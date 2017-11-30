/*Hurryhurry module
   RFID and Bluetooth
*/
#include <SPI.h>
#include <MFRC522.h>//RFID 
#include <SoftwareSerial.h> //시리얼통신 라이브러리 호출

#define RST_PIN 9
#define SS_PIN 10
#define BLUE_TX 2
#define BLUE_RX 3

MFRC522 rfid(SS_PIN, RST_PIN); // RFID 통신을 위한 객체 선언
SoftwareSerial btSerial(BLUE_TX, BLUE_RX);  //시리얼 통신을 위한 객체선언

void setup()
{
  Serial.begin(19200);   //시리얼모니터
  btSerial.begin(9600); //블루투스 시리얼
  SPI.begin();
  rfid.PCD_Init();
}
void loop()
{
  if ( !rfid.PICC_IsNewCardPresent() || !rfid.PICC_ReadCardSerial() ) {// 태그 접촉이 되지 않았을때 또는 ID가 읽혀지지 않았을때
    delay(500);                                // 0.5초 딜레이
    return;                                    // return
  }
  else {
    Serial.print("Card UID:");                  // 태그의 ID출력
    String temp = ""; 
    for (int i = 0; i < 4; i++) {               // 태그의 ID출력하는 반복문.태그의 ID사이즈(4)까지
      Serial.print(rfid.uid.uidByte[i]);        // rfid.uid.uidByte[0] ~ rfid.uid.uidByte[3]까지 출력
      Serial.print(" ");                        // id 사이의 간격 출력
      temp += rfid.uid.uidByte[i];
    }
    Serial.print("\n");
    btSerial.println(temp);
    temp = "";
  }
}

