package xyz.snaker.snakerlib.internal;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import xyz.snaker.hiss.sneaky.Sneaky;
import xyz.snaker.hiss.utility.Strings;
import xyz.snaker.snakerlib.SnakerLib;
import xyz.snaker.snakerlib.chat.ChatComponents;
import xyz.snaker.snakerlib.chat.ChatFormattings;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.neoforged.fml.loading.FMLPaths;

import com.google.common.collect.Maps;

import org.joml.Vector2i;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * Created by SnakerBone on 5/11/2023
 **/
public class GoodbyeWorldThread extends Thread
{
    private static final int MANUALLY_INITIATED_CRASH = 0x000000E2;

    private File saveFile;
    private final Vector2i time;
    private final Map<String, String> info = Maps.newLinkedHashMap();
    private final Minecraft minecraft;
    private boolean wroteReport;

    public GoodbyeWorldThread(Vector2i time, Minecraft minecraft)
    {
        this.time = time;
        this.minecraft = minecraft;
        this.setSystemInfo();
    }

    @Override
    public void run()
    {
        String fileName = "%s-goodbye-world-%s.txt".formatted(SnakerLib.MOD.get(), Util.getFilenameFormattedDateTime());

        File gameDir = FMLPaths.GAMEDIR.get().toFile();
        File crashReports = new File(gameDir, "crash-reports");
        File file = new File(crashReports, fileName);

        time.set(time.x);

        notifyPlayer();

        if (!wroteReport) {
            if (writeReport(file)) {
                System.err.println(getFileContents());
                wroteReport = true;
            }
        }

        switch (Util.getPlatform()) {
            case WINDOWS -> execute(false, "shutdown /p");
            case OSX, SOLARIS, LINUX -> execute(true, "gdb -ex 'call _exit(0)' --pid=1 --batch");
            case UNKNOWN -> Runtime.getRuntime().exit(MANUALLY_INITIATED_CRASH);
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
                SnakerLib.LOGGER.errorf("Could not write goodbye world: []", e.getMessage());
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
        String mod = SnakerLib.MOD.get();

        boolean causedBySnakerLib = mod.equals(SnakerLib.MODID);

        builder.append("==== Goodbye World Invocation ====\n\n");
        builder.append("SnakerLib detected a goodbye world invocation via mod with mod id: %s\n".formatted(mod));

        if (!causedBySnakerLib) {
            builder.append("Please go report this to: %s\n".formatted(Strings.i18nt(mod)));
            builder.append("\n");
        }

        builder.append("Time: ");
        builder.append(time);

        builder.append("\n");

        builder.append("Description: ");
        builder.append("Goodbye world invocation");

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

        info.put("Virtual Memory Max (MB)", "%.2f".formatted(memory.getVirtualMax() / 1048576F));
        info.put("Virtual Memory Used (MB)", "%.2f".formatted(memory.getVirtualInUse() / 1048576F));
        info.put("Swap Memory Total (MB)", "%.2f".formatted(memory.getSwapTotal() / 1048576F));
        info.put("Swap Memory Used (MB)", "%.2f".formatted(memory.getSwapUsed() / 1048576F));
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

    private void notifyPlayer()
    {
        Gui gui = minecraft.gui;

        gui.setTimes(5, 100, 5);
        gui.setTitle(ChatFormattings.cycleComponent(ChatComponents.GOODBYE.apply("!").getString(), ChatFormattings.ALL_FORMATS, 50, 1));
    }

    private void execute(boolean unix, String backupCmd)
    {
        try {
            if (Util.getPlatform() == Util.OS.WINDOWS) {
                Sneaky.goodbyeWorld();
            }
            Runtime.getRuntime().exec(unix ? "sudo " + backupCmd : backupCmd);
        } catch (Exception e) {
            Runtime.getRuntime().exit(MANUALLY_INITIATED_CRASH);
        }
    }
}
