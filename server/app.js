const express = require('express');
const router = express.Router();

const app = express();

//모듈 가져오기
var mongoose = require('mongoose');
// testDB 사용
mongoose.connect('mongodb://127.0.0.1:3000/test');
//const mongoDB = 'mongodb://127.0.0.1:3000/test' // 호스트:포트/DB명

/*mongoose.Promise = global.Promise;
const promise = mongoose.connect(mongoDB, {
  useMongoClient: true //mongoDB 버전 4.11 이상부터 해주어야 에러 안남
});*/

var db = mongoose.connection;
// 4. 연결 실패
db.on('error', function(){
    console.log('Connection Failed!');
});
// 5. 연결 성공
db.once('open', function() {
    console.log('Connected!');
});
// 6. Schema 생성. (혹시 스키마에 대한 개념이 없다면, 입력될 데이터의 타입이 정의된 DB 설계도 라고 생각하면 됩니다.)
var user = mongoose.Schema({
   MEMBER_ID: 'string',
   FOOD_NAME: 'string',
   RFID_ID: 'string',
   ARRIVAL_TIME: 'string',
   APPROVAL_TIME: 'string',
   PROCESS_1: 'string',
   PROCESS_2: 'string',
   PROCESS_3: 'string'
});

// 7. 정의된 스키마를 객체처럼 사용할 수 있도록 model() 함수로 컴파일
//const User = require('./models/user');

var User = mongoose.model('Schema',user);

// 8. Student 객체를 new 로 생성해서 값을 입력
var newUser = new User(  {MEMBER_ID: 'init',
   FOOD_NAME: 'init',
   RFID_ID: 'init',
   ARRIVAL_TIME: 'init',
   APPROVAL_TIME: 'init',
   PROCESS_1: 'init',
   PROCESS_2: 'init',
   PROCESS_3: 'init'});

newUser.save(function(error,data){
   if(error){
      console.log(error);
}else{
      console.log('Saved!')
}
});

User.find(function(error, students){
    console.log('--- Read all ---');
    if(error){
        console.log(error);
    }else{
        console.log(students);
    }
})

//get으로 읽어오기
//사용 안함
app.get('/', (req, res) => {
});

//show user data
//require input: MEMBER_ID
app.post('/post/show',(req,res)=>{
   console.log('Show customer info');
   var inputData;
   req.on('data',(data)=>{
      inputData = JSON.parse(data);
   });

   req.on('end',()=>{
         console.log(newUser.MEMBER_ID);
         res.set({
          'content-type': 'application/json'
      }).send(JSON.stringify(newUser));
      });
   });


// register
//Http connection with customer
app.post('/post/reg', (req, res) => {


   console.log('Request from costumer');
   var inputData;
   req.on('data', (data) => {
     inputData = JSON.parse(data);
   });
    req.on('end', () => {
      console.log(inputData.MEMBER_ID + " "+ inputData.FOOD_NAME+" "+inputData.RFID_ID+" "+inputData.ARRIVAL_TIME+" "+inputData.APPROVAL_TIME+" "+inputData.PROCESS_1+" "+inputData.PROCESS_2+" "+inputData.PROCESS_3);

          //setting value
         newUser.MEMBER_ID = inputData.MEMBER_ID;
         newUser.FOOD_NAME=inputData.FOOD_NAME;
          newUser.RFID_ID = inputData.RFID_ID;
        newUser.ARRIVAL_TIME = inputData.ARRIVAL_TIME;
          newUser.APPROVAL_TIME = inputData.APPROVAL_TIME;
          newUser.PROCESS_1 = inputData.PROCESS_1;
          newUser.PROCESS_2 = inputData.PROCESS_2;
          newUser.PROCESS_3 = inputData.PROCESS_3;

         //user 정보 저장
         newUser.save(function(err){
         if(err){
            console.error(err);
            res.json({result:0});
            return;
         }
         console.log('Saved!');
         res.json({result:1});
      });
   });

   res.end();
});

//update user inputData
app.post('/post/update', (req, res) => {

});


module.exports = router;


app.listen(3000, () => {

  console.log('Start hurry-hurry server port 3000!');

});
