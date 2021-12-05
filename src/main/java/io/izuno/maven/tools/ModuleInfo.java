package io.izuno.maven.tools;

public class ModuleInfo {

	private String artifactId;
	private String version;
	private String groupId;

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String toString() {
		return  "Group    : " + groupId + System.lineSeparator() +
				"Artifact : " + artifactId + System.lineSeparator() +
				"Version  : " + version + System.lineSeparator();
	}

}
