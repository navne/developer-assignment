package com.developer.assignment.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class CreateFolderImpl implements CreateFolder {

	Random r = new Random();

	@Override
	public void createFolder(List<String> basePath) {

		for (int i = 1; i < 6; i++) {

			String path = basePath.get(r.nextInt(basePath.size())) + "folder_" + i;
			boolean folderCreated = new File(path).mkdir();
			if (!folderCreated) {
				System.err.println("Error while generating folder");
			}
			basePath.add(path + File.separator);

		}

	}

	@Override
	public void createFile(List<String> extList, List<String> basePath) {

		for (int i = 1; i < 51; i++) {
			String ext = extList.get(r.nextInt(extList.size()));
			String content = getRandomString();
			String path = basePath.get(r.nextInt(basePath.size()));
			try {
				FileWriter writer = new FileWriter(path + i + "." + ext, true);
				writer.write(content);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private String getRandomString() {
		byte[] array = new byte[7];
		new Random().nextBytes(array);
		String generatedString = new String(array, Charset.forName("UTF-16"));

		List<String> randomString = new ArrayList<String>();
		randomString.add("MOBILE_NUMBER");
		randomString.add("EMAIL");
		randomString.add(generatedString);
		return randomString.get(r.nextInt(randomString.size()));

	}

	@Override
	public List<String> getListOfFiles(String basePath, String ext) {

		Path start = Paths.get(basePath);
		List<String> collect = null;
		try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
			collect = stream.filter(p -> !Files.isDirectory(p)).map(String::valueOf).sorted()
					.filter(f -> f.endsWith(ext)).collect(Collectors.toList());

		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(3);
		}
		return collect;

	}

	private void starCopy(List<String> collect, String basePath, String ext) {

		collect.forEach(c -> {
			Path path = Paths.get(c);

			try (Stream<String> stream1 = Files.lines(path)) {

				stream1.forEach(f -> {
					String pathDiff = StringUtils.difference(basePath, c);
					if (f.contains("EMAIL") || f.contains("MOBILE_NUMBER")) {
						copyFiles(c, basePath + File.separator + ext + "//quarrantine//" + pathDiff);
					} else {
						copyFiles(c, basePath + File.separator + ext + File.separator + pathDiff);
					}

				});

			} catch (IOException e1) {
				System.err.println(e1.getMessage());
				System.exit(3);
			}
		});

	}

	private void copyFiles(String source, String destination) {
		try {
			if (!Files.exists(Paths.get(destination).getParent())) {
				Files.createDirectories(Paths.get(destination).getParent());
			}
			Files.copy(Paths.get(source), Paths.get(destination));
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(2);
		}

	}

	public static class ParallelImplement implements Runnable {
		private final String ext;
		private final String basePath;
		private final List<String> collect;

		public ParallelImplement(String ext, String basePath, List<String> collect) {
			super();
			this.ext = ext;
			this.basePath = basePath;
			this.collect = collect;
		}

		@Override
		public void run() {
			Instant start = Instant.now();

			CreateFolderImpl createFoler = new CreateFolderImpl();
			createFoler.starCopy(collect, basePath, ext);
			Instant finish = Instant.now();
			long timeElapsed = Duration.between(start, finish).toMillis();
			System.out.println("Time elapsed " + timeElapsed);

		}
	}

	@Override
	public void copyOutputFiles(List<String> collect, String basePath, List<String> ext) {

		ExecutorService executorService = Executors.newFixedThreadPool(ext.size());

		ext.forEach(extension -> {
			Runnable worker = new ParallelImplement(extension, basePath, collect);
			executorService.execute(worker);

		});

		executorService.shutdown();
		// Wait until all threads are finish
		while (!executorService.isTerminated()) {

		}
		System.out.println("\nFinished all threads");
	}

}
