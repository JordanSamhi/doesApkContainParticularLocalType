package com.github.JordanSamhi.doesApkContainParticularLocalType;

import java.util.ArrayList;
import java.util.List;

import com.github.JordanSamhi.doesApkContainParticularLocalType.Utils.CommandLineOptions;

import soot.Local;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;

public class Main {

	public static void main(String[] args) {

		CommandLineOptions options = new CommandLineOptions(args);

		List<String> apk = new ArrayList<String>();
		apk.add(options.getApk());

		String androidJarPAth = options.getPlatforms();
		String type = options.getType();

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
						if(l.getType().toString().equals(type)) {
							System.out.println("[+] Apk contains " + type);
							System.exit(1);
						}
					}
				}
			}
		}
		System.out.println("[!] Apk does not contain " + type);
		System.exit(0);
	}
}