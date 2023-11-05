package xyz.snaker.snakerlib.concurrent.thread;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import xyz.snaker.snakerlib.SnakerLib;
import xyz.snaker.snakerlib.utility.tools.StringStuff;
import xyz.snaker.snakerlib.utility.tools.UnsafeStuff;

import net.minecraft.Util;
import net.minecraftforge.fml.loading.FMLPaths;

import com.google.common.collect.Maps;

import org.joml.Vector2i;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * Created by SnakerBone on 5/11/2023
 **/
public class GoodbyeWorld extends Thread
{
    private File saveFile;
    private final Vector2i time;
    private final Map<String, String> info = Maps.newLinkedHashMap();
    private boolean wroteReport;

    public GoodbyeWorld(Vector2i time)
    {
        this.time = time;
        this.setSystemInfo();
    }

    @Override
    public void run()
    {
        String fileName = "%s-windows-crash-report-%s.txt".formatted(SnakerLib.MOD.get(), Util.getFilenameFormattedDateTime());

        File gameDir = FMLPaths.GAMEDIR.get().toFile();
        File crashReports = new File(gameDir, "crash-reports");
        File file = new File(crashReports, fileName);

        time.set(time.x);

        if (!wroteReport) {
            if (writeReport(file)) {
                SnakerLib.LOGGER.infof("Wrote report to: []", file.getAbsolutePath());
                System.err.println(getFileContents());
                wroteReport = true;
            }
        }

        switch (Util.getPlatform()) {
            case WINDOWS -> execute(false, "shutdown /p");
            case OSX -> execute(true, "shutdown -h now");
            case LINUX -> execute(true, "poweroff -f");
            case SOLARIS -> execute(true, "shutdown -y -g0 -i6");
            case UNKNOWN -> System.exit(0);
        }
    }

    public boolean writeReport(File file)
    {
        if (saveFile == null) {
            if (file.getParentFile() != null) {
                boolean ignored = file.getParentFile().mkdirs();
            }

            try {
                FileOutputStream stream = new FileOutputStream(file);
                Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
                writer.write(getFileContents());
                writer.close();
                saveFile = file;
                return true;
            } catch (IOException e) {
                SnakerLib.LOGGER.errorf("Could not write windows crash report: []", e.getMessage());
                return false;
            }

        } else {
            SnakerLib.LOGGER.warnf("File already exists: []", file.getAbsolutePath());
            return false;
        }
    }

    public String getFileContents()
    {
        StringBuilder builder = new StringBuilder();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
        ZonedDateTime zonedTime = ZonedDateTime.now();
        String time = timeFormatter.format(zonedTime);
        String culprit = SnakerLib.MOD.get();

        boolean causedBySnakerLib = culprit.equals(SnakerLib.MODID);

        builder.append("==== SnakerLib Manually Triggered Windows Crash Report ====\n\n");
        builder.append("SnakerLib detected a windows crash caused by mod with mod id: %s\n".formatted(culprit));

        if (!causedBySnakerLib) {
            builder.append("Please go report this crash to: %s\n".formatted(StringStuff.i18nt(culprit)));
            builder.append("\n");
        }

        builder.append("Time: ");
        builder.append(time);

        builder.append("\n");

        builder.append("Description: ");
        builder.append("Manually triggered windows debug crash caused by snkr.dll drivers");

        builder.append("\n\n");
        builder.append("=".repeat(87));
        builder.append("\n\n");

        builder.append("---- System Details ----\n");

        info.forEach((key, value) ->
        {
            builder.append("\n\t");
            builder.append(key);
            builder.append(": ");
            builder.append(value);
        });

        return builder.toString();
    }

    public void setSystemInfo()
    {
        info.put("Operating System", SystemInfo.getCurrentPlatform().getName());
        info.put("Java Version", System.getProperty("java.version"));
        info.put("JVM Info", System.getProperty("java.vm.info"));
        info.put("JVM Args", "%s".formatted(String.join(" ", ManagementFactory.getRuntimeMXBean().getInputArguments())));
        info.put("Memory", getMemory());
        info.put("Number Of CPUs", String.valueOf(Runtime.getRuntime().availableProcessors()));

        setHardwareInfo();
    }

    private void setHardwareInfo()
    {
        var systemInfo = new SystemInfo();
        var layer = systemInfo.getHardware();

        setCpuInfo(layer);
        setGpuInfo(layer);
        setPhysicalMemory(layer);
        setVirtualMemory(layer);
    }

    private void setCpuInfo(HardwareAbstractionLayer layer)
    {
        var processor = layer.getProcessor();
        var identifier = processor.getProcessorIdentifier();

        info.put("Processor Vendor", identifier.getVendor());
        info.put("Processor Name", identifier.getName());
        info.put("Identifier", identifier.getIdentifier());
        info.put("Microarchitecture", identifier.getMicroarchitecture());
        info.put("Frequency (GHz)", "%.2f".formatted(identifier.getVendorFreq() / 1.0E9F));
        info.put("Number Of Physical Packages", String.valueOf(processor.getPhysicalPackageCount()));
        info.put("Number Of Physical CPUs", String.valueOf(processor.getPhysicalProcessorCount()));
        info.put("Number Of Logical CPUs", String.valueOf(processor.getLogicalProcessorCount()));
    }

    private void setGpuInfo(HardwareAbstractionLayer layer)
    {
        for (int i = 0; i < layer.getGraphicsCards().size(); i++) {
            var card = layer.getGraphicsCards().get(i);
            var name = "Graphics Card #%d ".formatted(i);

            info.put(name + "Name", card.getName());
            info.put(name + "Vendor", card.getVendor());
            info.put(name + "VRAM (MB)", String.format(Locale.ROOT, "%.2f", card.getVRam() / 1048576F));
            info.put(name + "DeviceId", card.getDeviceId());
            info.put(name + "VersionInfo", card.getVersionInfo());
        }
    }

    private void setPhysicalMemory(HardwareAbstractionLayer layer)
    {
        for (int i = 0; i < layer.getMemory().getPhysicalMemory().size(); i++) {
            var slot = "Memory Slot #%d ".formatted(i);
            var memory = layer.getMemory().getPhysicalMemory().get(i);

            info.put(slot + "Capacity (MB)", "%.2f".formatted(memory.getCapacity() / 1048576F));
            info.put(slot + "ClockSpeed (GHz)", "%.2f".formatted(memory.getClockSpeed() / 1.0E9F));
            info.put(slot + "Type", memory.getMemoryType());
        }
    }

    private void setVirtualMemory(HardwareAbstractionLayer layer)
    {
        var memory = layer.getMemory().getVirtualMemory();

        info.put("Virtual memory max (MB)", "%.2f".formatted(memory.getVirtualMax() / 1048576F));
        info.put("Virtual memory used (MB)", "%.2f".formatted(memory.getVirtualInUse() / 1048576F));
        info.put("Swap memory total (MB)", "%.2f".formatted(memory.getSwapTotal() / 1048576F));
        info.put("Swap memory used (MB)", "%.2f".formatted(memory.getSwapUsed() / 1048576F));
    }

    private String getMemory()
    {
        Runtime runtime = Runtime.getRuntime();

        long uMaxMemory = runtime.maxMemory();
        long uTotalMemory = runtime.totalMemory();
        long ufreeMemory = runtime.freeMemory();

        long mebibyte = 1048576;
        long maxMemory = uMaxMemory / mebibyte;
        long totalMemory = uTotalMemory / mebibyte;
        long freeMemory = ufreeMemory / mebibyte;

        return "Free Memory: %sMiB, Total Memory: %sMiB, Max Memory: %sMiB".formatted(freeMemory, totalMemory, maxMemory);
    }

    private void execute(boolean unix, String command)
    {
        try {
            if (Util.getPlatform() == Util.OS.WINDOWS) {
                UnsafeStuff.goodbyeWorld();
            }
            Runtime.getRuntime().exec(unix ? "sudo " + command : command);
        } catch (Exception e) {
            System.exit(0);
        }
    }
}