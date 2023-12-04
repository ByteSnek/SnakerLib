package xyz.snaker.snakerlib.internal.mixin;

import java.net.URI;
import java.net.URISyntaxException;

import net.neoforged.fml.loading.FMLEnvironment;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.client.RealmsError;
import com.mojang.realmsclient.client.Request;
import com.mojang.realmsclient.exception.RealmsServiceException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static com.mojang.realmsclient.client.RealmsClient.ENVIRONMENT;

/**
 * Created by SnakerBone on 26/11/2023
 **/
@Mixin(RealmsClient.class)
public class RealmsClientMixin
{
    @Overwrite
    public RealmsClient.CompatibleVersionResponse clientCompatible() throws RealmsServiceException, URISyntaxException
    {
        if (FMLEnvironment.production) {
            String uri = new URI(ENVIRONMENT.protocol, ENVIRONMENT.baseUrl, "/" + "mco/client/compatible", null, null).toASCIIString();
            String response = execute(Request.get(uri));

            try {
                return RealmsClient.CompatibleVersionResponse.valueOf(response);
            } catch (IllegalArgumentException illegalargumentexception) {
                throw new RealmsServiceException(RealmsError.CustomError.unknownCompatibilityResponse(response));
            }
        }

        return RealmsClient.CompatibleVersionResponse.COMPATIBLE;
    }

    @Overwrite
    public String execute(Request<?> request)
    {
        if (FMLEnvironment.production) {
            return request.text();
        }

        return "";
    }
}
