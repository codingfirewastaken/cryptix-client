package cryptix.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Module;
import cryptix.module.visual.ClickGUI;

public class JsonHandler {
	public static File ROOT_DIR = new File("cryptix");
    public static File config = new File(ROOT_DIR, "config");
    public static File modules = new File(config, "latest.json");
    public static File keybinds = new File(ROOT_DIR, "keybinds.json");
    private static HashSet<String> modBlackList = Sets.newHashSet(ClickGUI.class.getName());
    public static Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    public static JsonParser jsonParser = new JsonParser();

    public static void init() {
        if (!ROOT_DIR.exists()) { ROOT_DIR.mkdirs(); }
        if (!config.exists()) { config.mkdirs(); }

        if (!modules.exists())
            saveMods();
        else
            loadMods();

        if (!keybinds.exists())
            saveKeybinds();
        else
            loadKeybinds();
    }

    public static void saveMods() {
        try {
            JsonObject json = new JsonObject();
            for (Module mod : Client.instance.moduleManager.getModules()) {
                JsonObject jsonMod = new JsonObject();
                jsonMod.addProperty("enabled", mod.isToggled());

                JsonObject jsonSettings = new JsonObject();
                for (Setting setting : mod.getSettings()) {
                    if (setting.isCombo()) {
                        jsonSettings.addProperty(setting.getName(), Client.instance.settingsManager.getSettingByName(mod, setting.getName()).getString());
                    } else if (setting.isCheck()) {
                        jsonSettings.addProperty(setting.getName(), Client.instance.settingsManager.getSettingByName(mod, setting.getName()).getBoolean());
                    } else if (setting.isSlider()) {
                        jsonSettings.addProperty(setting.getName(), Client.instance.settingsManager.getSettingByName(mod, setting.getName()).getValue());
                    }
                }
                jsonMod.add("settings", jsonSettings);
                json.add(mod.getName(), jsonMod);
            }
            PrintWriter save = new PrintWriter(new FileWriter(modules));
            save.println(prettyGson.toJson(json));
            save.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JsonObject jsonKeybinds = new JsonObject();
            for (Module mod : Client.instance.moduleManager.getModules()) {
                System.out.println("Saving Keybind - Module: " + mod.getName() + ", Key: " + mod.getKey());
                jsonKeybinds.addProperty(mod.getName(), mod.getKey());
            }

            PrintWriter save = new PrintWriter(new FileWriter(keybinds));
            save.println(prettyGson.toJson(jsonKeybinds));
            save.close();

            System.out.println("Keybinds saved successfully to " + keybinds.getAbsolutePath());

        } catch (Exception e) {
            System.err.println("Error while saving keybinds:");
            e.printStackTrace();
        }
    }
    
    public static void saveMods(String configName) {
    	File configs = new File(config, configName + ".json");
        try {
            JsonObject json = new JsonObject();
            for (Module mod : Client.instance.moduleManager.getModules()) {
                JsonObject jsonMod = new JsonObject();
                jsonMod.addProperty("enabled", mod.isToggled());
                JsonObject jsonSettings = new JsonObject();
                for (Setting setting : mod.getSettings()) {
                    if (setting.isCombo()) {
                        jsonSettings.addProperty(setting.getName(), Client.instance.settingsManager.getSettingByName(mod, setting.getName()).getString());
                    } else if (setting.isCheck()) {
                        jsonSettings.addProperty(setting.getName(), Client.instance.settingsManager.getSettingByName(mod, setting.getName()).getBoolean());
                    } else if (setting.isSlider()) {
                        jsonSettings.addProperty(setting.getName(), Client.instance.settingsManager.getSettingByName(mod, setting.getName()).getValue());
                    }
                }
                jsonMod.add("settings", jsonSettings);
                json.add(mod.getName(), jsonMod);
            }
            PrintWriter save = new PrintWriter(new FileWriter(configs));
            save.println(prettyGson.toJson(json));
            save.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadMods() {
        try {
            BufferedReader load = new BufferedReader(new FileReader(modules));
            JsonObject json = (JsonObject) jsonParser.parse(load);
            load.close();
            for (Entry<String, JsonElement> entry : json.entrySet()) {
                Module mod = Client.instance.moduleManager.getModuleByName(entry.getKey());
                JsonObject jsonModule = (JsonObject) entry.getValue();
                if (mod != null && !modBlackList.contains(mod.getClass().getName())) {
                    boolean enabled = jsonModule.get("enabled").getAsBoolean();

                    if (enabled) {
                        mod.toggle();
                    }
                }
                JsonObject jsonSettings = jsonModule.getAsJsonObject("settings");
                if (jsonSettings != null && mod.getSettings() != null) {
                    for (Setting setting : mod.getSettings()) {
                        if (jsonSettings.has(setting.getName())) {
                            if (setting.isCombo()) {
                                String value = jsonSettings.get(setting.getName()).getAsString();
                                Client.instance.settingsManager.getSettingByName(mod, setting.getName()).setString(value);
                            } else if (setting.isCheck()) {
                                boolean value = jsonSettings.get(setting.getName()).getAsBoolean();
                                Client.instance.settingsManager.getSettingByName(mod, setting.getName()).setBoolean(value);
                            } else if (setting.isSlider()) {
                                double value = jsonSettings.get(setting.getName()).getAsDouble();
                                Client.instance.settingsManager.getSettingByName(mod, setting.getName()).setValue(value);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadMods(String configName) {
    	File configs = new File(config, configName + ".json");
    	try {
             BufferedReader load = new BufferedReader(new FileReader(configs));
             JsonObject json = (JsonObject) jsonParser.parse(load);
             load.close();
             for (Entry<String, JsonElement> entry : json.entrySet()) {
                 Module mod = Client.instance.moduleManager.getModuleByName(entry.getKey());
                 JsonObject jsonModule = (JsonObject) entry.getValue();
                 if (mod != null ) {
                     boolean enabled = jsonModule.get("enabled").getAsBoolean();

                     if (enabled && !mod.isToggled()) {
                         mod.toggle();
                     }
                     if (!enabled && mod.isToggled()) {
                         mod.toggle();
                     }
                 }
                 JsonObject jsonSettings = jsonModule.getAsJsonObject("settings");
                 if (jsonSettings != null && mod.getSettings() != null) {
                     for (Setting setting : mod.getSettings()) {
                         if (jsonSettings.has(setting.getName())) {
                             if (setting.isCombo()) {
                                 String value = jsonSettings.get(setting.getName()).getAsString();
                                 Client.instance.settingsManager.getSettingByName(mod, setting.getName()).setString(value);
                             } else if (setting.isCheck()) {
                                 boolean value = jsonSettings.get(setting.getName()).getAsBoolean();
                                 Client.instance.settingsManager.getSettingByName(mod, setting.getName()).setBoolean(value);
                             } else if (setting.isSlider()) {
                                 double value = jsonSettings.get(setting.getName()).getAsDouble();
                                 Client.instance.settingsManager.getSettingByName(mod, setting.getName()).setValue(value);
                             }
                         }
                     }
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
    }

    public static void saveKeybinds() {
        try {
            JsonObject jsonKeybinds = new JsonObject();
            for (Module mod : Client.instance.moduleManager.getModules()) {
                System.out.println("Saving Keybind - Module: " + mod.getName() + ", Key: " + mod.getKey());
                jsonKeybinds.addProperty(mod.getName(), mod.getKey());
            }

            PrintWriter save = new PrintWriter(new FileWriter(keybinds));
            save.println(prettyGson.toJson(jsonKeybinds));
            save.close();

            System.out.println("Keybinds saved successfully to " + keybinds.getAbsolutePath());

        } catch (Exception e) {
            System.err.println("Error while saving keybinds:");
            e.printStackTrace();
        }
    }


    public static void loadKeybinds() {
        try {
            BufferedReader load = new BufferedReader(new FileReader(keybinds));
            JsonObject jsonKeybinds = (JsonObject) jsonParser.parse(load);
            load.close();

            System.out.println("Loaded keybinds:");
            for (Entry<String, JsonElement> entry : jsonKeybinds.entrySet()) {
                Module mod = Client.instance.moduleManager.getModuleByName(entry.getKey());
                if (mod != null) {
                    JsonElement value = entry.getValue();
                    if (value != null && value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber()) {
                        int keybind = value.getAsInt();
                        mod.setKey(keybind);
                        System.out.println("Module: " + mod.getName() + ", Keybind: " + keybind);
                    } else {
                        System.out.println("Warning: Invalid keybind value for module " + entry.getKey());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
