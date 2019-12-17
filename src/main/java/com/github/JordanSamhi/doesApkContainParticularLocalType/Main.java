package com.github.JordanSamhi.doesApkContainParticularLocalType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.JordanSamhi.doesApkContainParticularLocalType.Utils.CommandLineOptions;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		CommandLineOptions options = new CommandLineOptions(args);

		String apk = options.getApk();
		String platforms = options.getPlatforms();
		String type = options.getType();

		InfoflowAndroidConfiguration ifac = new InfoflowAndroidConfiguration();
		ifac.setIgnoreFlowsInSystemPackages(false);
		SetupApplication sa = null;
		ifac.getAnalysisFileConfig().setAndroidPlatformDir(platforms);
		ifac.getAnalysisFileConfig().setTargetAPKFile(apk);
		sa = new SetupApplication(ifac);
		sa.constructCallgraph();

		for(SootClass sc : Scene.v().getApplicationClasses()) {
			for(SootMethod sm : sc.getMethods()) {
				if(sm.hasActiveBody()) {
					for (Local l : sm.getActiveBody().getLocals()) {
						if(l.getType().toString().equals(type)) {
							logger.info("Apk contains " + type);
							System.exit(1);
						}
					}
				}
			}
		}
		logger.warn("Apk does not contain " + type);
		System.exit(0);
	}
}