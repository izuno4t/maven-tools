package io.izuno.maven.tools;

public class ModuleInfo {

	private final String artifactId;
	private final String version;
	private final String groupId;

	public ModuleInfo(String artifactId, String groupId, String version) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}

	public String getArtifactId() {
		return artifactId;
	}


	public String getVersion() {
		return version;
	}


	public String getGroupId() {
		return groupId;
	}


	public String toString() {
		return  "Group    : " + groupId + System.lineSeparator() +
				"Artifact : " + artifactId + System.lineSeparator() +
				"Version  : " + version + System.lineSeparator();
	}

}
