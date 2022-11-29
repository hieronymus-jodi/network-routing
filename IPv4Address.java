// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// Class representing an IPv4 address. For simplicity, only Class A IPv4 addresses are used.
public class IPv4Address {
    // Using ints because Java does not natively have unsigned bytes
    int byte1, byte2, byte3, byte4;

    IPv4Address(int byte1, int byte2, int byte3, int byte4) {
        this.byte1 = byte1;
        this.byte2 = byte2;
        this.byte3 = byte3;
        this.byte4 = byte4;

        // Print warning if address invalid
        boolean invalid = false;
        // Class A, so limit byte1: 1-127
        if (byte1 < 1 || byte1 > 127) {
            invalid = true;
        }
        // Limit other bytes: 0-255
        else if ((byte2 < 0 || byte2 > 255) || (byte3 < 0 || byte3 > 255) || (byte4 < 0 || byte4 > 255)) {
            invalid = true;
        }
        if (invalid) {
            System.out.println("WARNING: This address is not a valid Class A IPv4 address: ");
            System.out.println("        -> " + this.getAddress());
        }
    }

    // Used to see network
    public int getNetwork() {
        return byte1;
    }

    public String getAddress() {
        return byte1 + "." + byte2 + "." + byte3 + "." + byte4;
    }
}
