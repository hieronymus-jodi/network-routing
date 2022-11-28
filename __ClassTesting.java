// Jodi Hieronymus - CPE 400 Final Project - Fall 2022

// Testing to make sure classes are functioning as intended
public class __ClassTesting {
    public static void main(String[] args) {
      System.out.println("---- ---- ---- TESTING CLASSES ---- ---- ----");
      IPv4Address_Testing();
      Packet_Testing();
      Router_Testing();
    }

    private static void IPv4Address_Testing() {
      System.out.println("* ---- IPv4Address ---- *");
      new IPv4Address(0, 0, 0, 0); // Should be invalid

      IPv4Address validAddress = new IPv4Address(121, 0, 1, 2);
      System.out.println(validAddress.getAddress() + ".getNetwork(): " + validAddress.getNetwork() + "\n");
    }

    private static void Packet_Testing() {
      System.out.println("* ---- Packet ---- *");
      IPv4Address sourceAddress = new IPv4Address(121, 0, 1, 2);
      IPv4Address destinationAddress = new IPv4Address(119, 0, 2, 34);
      Packet testPacket = new Packet(sourceAddress, destinationAddress, "Hello!");

      System.out.println("Packet source address: " + testPacket.getSourceAddress().getAddress());
      System.out.println("Packet destination address: " + testPacket.getDestinationAddress().getAddress());
      System.out.println("Packet application data: " + testPacket.getApplicationData() + "\n");
    }

    private static void Router_Testing() {
      System.out.println("* ---- Router ---- *");

      IPv4Address sourceAddress = new IPv4Address(121, 0, 1, 2);
      IPv4Address destinationAddress = new IPv4Address(119, 0, 2, 34);
      Packet testPacket = new Packet(sourceAddress, destinationAddress, "Hello!");
      Router destinationRouter = new Router(119, null, null);

      destinationRouter.routePacket(testPacket);
    }
}
