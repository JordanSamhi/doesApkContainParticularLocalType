package com.github.JordanSamhi.doesApkContainParticularLocalType;

import java.util.ArrayList;
import java.util.List;

import soot.Local;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;

public class Main {

	public static void main(String[] args) {
		List<String> apk = new ArrayList<String>();
		apk.add(args[0]);
		
		String androidJarPAth = "";

		soot.G.reset();
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_force_android_jar(androidJarPAth);
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_process_dir(apk);
		Scene.v().loadNecessaryClasses();
		Options.v().set_output_format(Options.output_format_none);
		PackManager.v().runPacks();

		for(SootClass sc : Scene.v().getApplicationClasses()) {
			for(SootMethod sm : sc.getMethods()) {
				if(sm.hasActiveBody()) {
					for (Local l : sm.getActiveBody().getLocals()) {
						if(l.getType().toString().equals("android.app.AlarmManager")) {
							System.exit(1);
						}
					}
				}
			}
		}
		System.exit(0);
	}
}