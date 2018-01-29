package codechicken.core;

import codechicken.core.launch.CodeChickenCorePlugin;
import com.google.common.base.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.versioning.ComparableVersion;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

public class CCUpdateChecker {
    private static final ArrayList<String> updates = new ArrayList<String>();

    private static class ThreadUpdateCheck extends Thread {
        private final URL url;
        private final Function<String, Void> handler;

        public ThreadUpdateCheck(URL url, Function<String, Void> handler) {
            this.url = url;
            this.handler = handler;

            setName("CodeChicken Update Checker");
        }

        @Override
        public void run() {
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String ret = read.readLine();
                read.close();
                if (ret == null) {
                    ret = "";
                }
                handler.apply(ret);
            } catch (SocketTimeoutException ignored) {
            } catch (UnknownHostException ignored) {
            } catch (IOException iox) {
                iox.printStackTrace();
            }
        }
    }

    public static void tick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (!mc.inGameHasFocus) {
            return;
        }

        synchronized (updates) {
            for (String updateMessage : updates) {
                mc.thePlayer.addChatMessage(new TextComponentString(updateMessage));
            }
            updates.clear();
        }
    }

    public static void addUpdateMessage(String s) {
        synchronized (updates) {
            updates.add(s);
        }
    }

    public static String mcVersion() {
        return (String) FMLInjectionData.data()[4];
    }

    public static void updateCheck(final String mod, final String version) {
        updateCheck("http://www.chickenbones.net/Files/notification/version.php?" +
                "version=" + mcVersion() + "&" +
                "file=" + mod, new Function<String, Void>() {
            @Override
            public Void apply(String ret) {
                if (!ret.startsWith("Ret: ")) {
                    CodeChickenCorePlugin.logger.error("Failed to check update for " + mod + " returned: " + ret);
                    return null;
                }
                ComparableVersion newVersion = new ComparableVersion(ret.substring(5));
                if (newVersion.compareTo(new ComparableVersion(version)) > 0) {
                    ModDescriptionEnhancer.setUpdateStatus(mod, ForgeVersion.Status.OUTDATED, newVersion);
                    addUpdateMessage("Version " + newVersion + " of " + mod + " is available");
                }
                return null;
            }
        });
    }

    public static void updateCheck(String mod) {
        updateCheck(mod, Loader.instance().getIndexedModList().get(mod).getVersion());
    }

    public static void updateCheck(String url, Function<String, Void> handler) {
        try {
            new ThreadUpdateCheck(new URL(url), handler).start();
        } catch (MalformedURLException e) {
            CodeChickenCorePlugin.logger.error("Malformed URL: " + url, e);
        }
    }
}
