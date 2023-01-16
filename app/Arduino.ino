#include <ESP8266WiFi.h>
#include <WiFiUdp.h>

const char* ssid = "BSR - Nauczyciele";
const char* password = "realn@2015";



WiFiUDP Udp;
unsigned int localPort = 8080;  // local port to listen on

class Motor{
  const int m1, m2;
  bool is_reversed;

  public:
  Motor(int m1_pin, int m2_pin):
  m1(m1_pin), m2(m2_pin){
    pinMode(m1, OUTPUT);
    pinMode(m2, OUTPUT);
  }
  Motor(int m1_pin, int m2_pin, bool reverse):
  m1(m1_pin), m2(m2_pin),is_reversed(reverse){
    pinMode(m1, OUTPUT);
    pinMode(m2, OUTPUT);
  }


  bool get_reversed(){
    return is_reversed;
  }
  void reverse(){
    this->is_reversed = !this->is_reversed;
  }

  void forward(){
    if (is_reversed){
      digitalWrite(m1, LOW);
      digitalWrite(m2, HIGH);
    }
    else{
      digitalWrite(m1, HIGH);
      digitalWrite(m2, LOW);
    }
    Serial.println("forward");

  }

  void backward(){
    if (!is_reversed){
      digitalWrite(m1, LOW);
      digitalWrite(m2, HIGH);
    }
    else{
      digitalWrite(m1, HIGH);
      digitalWrite(m2, LOW);
    }
    Serial.println("backward");
  }
  void stop(){
    digitalWrite(m1, LOW);
    digitalWrite(m2, LOW);
        Serial.println("stop");

  }
};


Motor m1(5,4,0), m2(14,12,1);


void setup() {
  Serial.begin(9600);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }
  Serial.println("Connected to WiFi");
  Udp.begin(localPort);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  pinMode(5,OUTPUT);
  pinMode(4,OUTPUT);
}

void loop() {
  int packetSize = Udp.parsePacket();
  if (packetSize) {
    // receive incoming UDP packets
    Serial.print("Received packet of size ");
    Serial.println(packetSize);
    Serial.print("From ");
    IPAddress remoteIp = Udp.remoteIP();
    Serial.print(remoteIp);
    Serial.print(", port ");
    Serial.println(Udp.remotePort());

    // read the packet into packetBufffer
    char packetBuffer[UDP_TX_PACKET_MAX_SIZE];
    int len = Udp.read(packetBuffer, UDP_TX_PACKET_MAX_SIZE);
    if (len > 0) {
      packetBuffer[len] = 0;
    }
    Serial.println("Contents:");
    Serial.println(packetBuffer);
    String s(packetBuffer);

    if (s == "UP"){
      m1.forward();m2.forward();
    }else if (s == "DOWN"){
      m2.backward(); m1.backward();
    }else if (s == "LEFT"){
      m1.forward(); m2.backward();
    }else if (s == "RIGHT"){
      m1.backward(); m2.forward();
    }else{
      Serial.println("DONTKNOWWOTUMEAN");
    }
    delay(500);
    m1.stop();
    m2.stop();
    // send a reply to the IP address and port that sent us the packet we received
    Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
    Udp.write("ack");
    Udp.endPacket();
  }
  // m1.forward();
  // m2.forward();
  // delay(500);
  // m1.stop();
  // m2.stop();
  // delay(500);
  // Serial.print("forwa\n");
  // digitalWrite(5, HIGH);
  // digitalWrite(4, LOW);
  // delay(500);
  // digitalWrite(5, LOW);
  }