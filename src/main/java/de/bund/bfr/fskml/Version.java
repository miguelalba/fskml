package de.bund.bfr.fskml;

import java.util.Objects;

public class Version {

    public final int major;
    public final int minor;
    public final int patch;

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    @Override
    public String toString() {
        return String.format("Version [%d.%d.%d]", major, minor, patch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Version other = (Version) obj;
        return other.major == major && other.minor == minor && other.patch == patch;
    }
}
