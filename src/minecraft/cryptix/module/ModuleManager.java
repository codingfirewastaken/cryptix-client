package cryptix.module;

import java.util.ArrayList;
import java.util.function.Predicate;

import cryptix.Client;
import cryptix.module.combat.*;
import cryptix.module.config.BlocksMC;
import cryptix.module.exploit.*;
import cryptix.module.movement.*;
import cryptix.module.player.*;
import cryptix.module.visual.*;
import net.minecraft.client.settings.KeyBinding;

public class ModuleManager {
	private static ArrayList<Module> modules = new ArrayList<Module>();
	public Criticals criticals;
	public KillAura killAura;
	public AutoLogin autologin;
	public PlayerCrasher playerCrasher;
	public NoSlow noslow;
	public Sprint sprint;
	public AntiVoid antiVoid;
	public BedNuker bedNuker;
	public NoFall noFall;
	public Phase phase;
	public SafeWalk safeWalk;
	public Animations aminations;
	public ClickGUI clickGUI;
	public FreeLook freeLook;
	public SessionInfo sessionInfo;
	
	public ModuleManager() {
		//Modules goes here
		//Combat
		modules.add(new AntiBot());
		modules.add(new AutoClicker());
		modules.add(new Velocity());
		modules.add(criticals = new Criticals());
		modules.add(killAura = new KillAura());
		//Config
		modules.add(new BlocksMC());
		//Exploit
		modules.add(new AntiCheat());
		modules.add(autologin = new AutoLogin());
		modules.add(new Disabler());
		modules.add(new StaffDetector());
		modules.add(playerCrasher = new PlayerCrasher());
		//Movement
		modules.add(new GroundSpeed());
		modules.add(new Fly());
		modules.add(new InvMove());
		modules.add(new KeepSprint());
		modules.add(new LongJump());
		modules.add(noslow = new NoSlow());
		modules.add(new Speed());
		modules.add(sprint = new Sprint());
		modules.add(new Step());
		modules.add(new TargetStrafe());
		//Player
		modules.add(antiVoid = new AntiVoid());
		modules.add(bedNuker = new BedNuker());
		modules.add(new ChestStealer());
		modules.add(new FastPlace());
		modules.add(new InvManager());
		modules.add(new Jesus());
		modules.add(noFall = new NoFall());
		modules.add(phase = new Phase());
		modules.add(safeWalk = new SafeWalk());
		modules.add(new Scaffold());
		//Visual
		modules.add(aminations = new Animations());
		modules.add(new BedESP());
		modules.add(new Chams());
		modules.add(new ChestESP());
		modules.add(clickGUI = new ClickGUI());
		modules.add(freeLook = new FreeLook());
		modules.add(new HUD());
		modules.add(new ItemESP());
		modules.add(new NameTags());
		modules.add(new PlayerESP());
		modules.add(sessionInfo = new SessionInfo());
		modules.add(new TargetHUD());
	}
	
	public ArrayList<Module> getModules() {
        return modules;
    }
    
    public Module getModuleByName(final String name) {
        return modules.stream().filter(new Predicate<Module>() {
			@Override
			public boolean test(Module module) {
				return module.getName().equalsIgnoreCase(name);
			}
		}).findFirst().orElse(null);
    }
    
    public static void onKey(int key) {
    	for(Module mod : modules) {
    		if(mod.getKey() == key) mod.toggle();
    	}
    	if(Client.instance.moduleManager.getModuleByName("Speed").isToggled() && !Client.instance.moduleManager.getModuleByName("Scaffold").isToggled()) {
    		KeyBinding.setKeyBindState(Client.mc.gameSettings.keyBindJump.getKeyCode(), false);
    	}
    }
    
    public ArrayList<Module> getModulesByCategory(Category c) {
    	ArrayList<Module> mods = new ArrayList<Module>();
    	for(Module mod : modules) {
    		if(mod.getCategory() == c){
    			mods.add(mod);
    		}
    	}
        return mods;
    }
}
