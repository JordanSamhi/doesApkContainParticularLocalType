package com.github.JordanSamhi.doesApkContainParticularLocalType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.JordanSamhi.doesApkContainParticularLocalType.Utils.CommandLineOptions;

import soot.Body;
import soot.G;
import soot.Local;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.options.Options;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		CommandLineOptions options = new CommandLineOptions(args);

		String apk = options.getApk();
		String platforms = options.getPlatforms();
		String type = options.getType();

		initializeSoot(platforms, apk);

		PackManager.v().getPack("wjtp").add(
				new Transform("wjtp.myTransform", new SceneTransformer() {
					protected void internalTransform(String phaseName,
							@SuppressWarnings("rawtypes") Map options) {
						for(SootClass sc : Scene.v().getApplicationClasses()) {
							if(!isSystemClass(sc.getName()) && sc.isConcrete()) {
								for(SootMethod sm : sc.getMethods()) {
									if(sm.isConcrete()) {
										Body b = sm.retrieveActiveBody();
										for (Local l : b.getLocals()) {
											if(l.getType().toString().equals(type)) {
												logger.info(String.format("Apk contains %s", type));
												System.exit(1);
											}
										}
									}
								}
							}
						}
					}
				}));
		PackManager.v().runPacks();
		logger.warn("Apk does not contain " + type);
		System.exit(0);
	}

	private static void initializeSoot(String platforms, String apk) {
		G.reset();
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_android_jars(platforms);
		Options.v().set_whole_program(true);
		List<String> apps = new ArrayList<String>();
		apps.add(apk);
		Options.v().set_process_dir(apps);
		Scene.v().loadNecessaryClasses();
	}

	// Inspired by Flowdroid
	private static boolean isSystemClass(String className) {
		return (className.startsWith("android.") || className.startsWith("java.") || className.startsWith("javax.")
				|| className.startsWith("sun.") || className.startsWith("org.omg.")
				|| className.startsWith("org.w3c.dom.") || className.startsWith("com.google.")
				|| className.startsWith("com.android."));
	}
}