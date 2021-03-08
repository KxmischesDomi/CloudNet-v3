package de.dytanic.cloudnet.ext.simplenametags.models;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class CloudNetTabProperties {

	private final String name;
	private final String prefix;
	private final String display;
	private final String color;
	private final String suffix;
	private final int sortId;

	public CloudNetTabProperties(@Nonnull String name, @Nullable String prefix, @Nullable String display, @Nullable String color, @Nullable String suffix, int sortId) {
		this.name = name;
		this.prefix = prefix;
		this.display = display;
		this.color = color;
		this.suffix = suffix;
		this.sortId = sortId;
	}

	public String getName() {
		return name;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getDisplay() {
		return display;
	}

	public String getColor() {
		return color;
	}

	public String getSuffix() {
		return suffix;
	}

	public int getSortId() {
		return sortId;
	}

	@Override
	public String toString() {
		return "TabProperties{" +
				"name='" + name + '\'' +
				", prefix='" + prefix + '\'' +
				", display='" + display + '\'' +
				", color='" + color + '\'' +
				", suffix='" + suffix + '\'' +
				", sortId=" + sortId +
				'}';
	}

}