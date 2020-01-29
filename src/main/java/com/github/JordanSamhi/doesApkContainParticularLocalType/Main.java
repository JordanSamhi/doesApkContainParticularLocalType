package com.github.JordanSamhi.doesApkContainParticularLocalType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.JordanSamhi.doesApkContainParticularLocalType.Utils.CommandLineOptions;

import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.PackManager;
import soot.Scene;
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

		PackManager.v().getPack("jtp").add(
				new Transform("jtp.myTransform", new BodyTransformer() {
					protected void internalTransform(Body body, String phase, @SuppressWarnings("rawtypes") Map options) {
						for (Local l : body.getLocals()) {
							if(l.getType().toString().equals(type)) {
								logger.info(String.format("Apk contains %s", type));
								logger.info(String.format("-- Method: %s", body.getMethod()));
								System.exit(1);
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
		Options.v().set_output_dir("/dev/null");
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_android_jars(platforms);
		List<String> apps = new ArrayList<String>();
		apps.add(apk);
		Options.v().set_process_dir(apps);
		Scene.v().loadNecessaryClasses();
	}
}