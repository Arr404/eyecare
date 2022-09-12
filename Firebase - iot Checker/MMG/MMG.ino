
#include <Servo.h> 

Servo servoKu;

void setup (){
  servoKu.attach(D2);
}

void loop(){
  servoKu.write(0);
  delay (1000);
  servoKu.write (180);
  delay (1000);

}
