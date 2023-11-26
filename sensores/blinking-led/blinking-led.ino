void setup() {
  // put your setup code here, to run once:

  // initialize digital pin LED_BUILTIN as an output
  pinMode(LED_BUILTIN, OUTPUT);
  // opens serial port, sets data rate to 9600 bps
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:

  Serial.println("LED: ON");
  // turn the LED on (HIGH is the voltage level) 
  digitalWrite(LED_BUILTIN, HIGH);
  // wait for a second
  delay(1000);
  Serial.println("LED: OFF");
  // turn the LED off by making the voltage LOW
  digitalWrite(LED_BUILTIN, LOW);
  // wait for a second
  delay(1000);
}