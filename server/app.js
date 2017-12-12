const express = require('express');
const router = express.Router();
const User = require('./models/user');
const app = express();
var mongoose = require('mongoose');
//mongoose.connect('mongodb://127.0.0.1:3000/test');
//const mongoDB = 'mongodb://127.0.0.1:3000/test' // 호스트:포트/DB명
/*mongoose.Promise = global.Promise;
const promise = mongoose.connect(mongoDB, {
  useMongoClient: true //mongoDB 버전 4.11 이상부터 해주어야 에러 안남
});
*/

//get으로 읽어오기
app.get('/', (req, res) => {
});


//Http connection with customer
app.post('/post', (req, res) => {

	const user = new User();

   console.log('Request from costumer');
   var inputData;
   req.on('data', (data) => {

     inputData = JSON.parse(data);

   });
    req.on('end', () => {
		console.log("Member id: "+inputData.MEMBER_ID);
		 console.log("Food name: "+inputData.FOOD_NAME);
		 console.log("RFID id : "+inputData.RFID_ID);
		 console.log("Arrival time: "+inputData.ARRIVAL_TIME);
		 console.log("Approval time: "+inputData.APPROVAL_TIME);
		 console.log("Process 1: "+inputData.PROCESS_1);
		 console.log("Process 2: "+inputData.PROCESS_2);
		 console.log("Process 3: "+inputData.PROCESS_3);

		 	//setting value
			user.MEMBER_ID = inputData.MEMBER_ID;
			user.FOOD_NAME=inputData.FOOD_NAME;
	 		user.RFID_ID = inputData.RFID_ID;
		  user.ARRIVAL_TIME = inputData.ARRIVAL_TIME;
		 	user.APPROVAL_TIME = inputData.APPROVAL_TIME;
		 	user.PROCESS_1 = inputData.PROCESS_1;
		 	user.PROCESS_2 = inputData.PROCESS_2;
		 	user.PROCESS_3 = inputData.PROCESS_3;

			//user 정보 저장
			user.save(function(err){
			if(err){
				console.error(err);
				res.json({result:0});
				return;
			}
			res.json({result:1});
		});
   });

   res.write("OK!");
   res.end();
});

module.exports = router;


//Http connection with restaurant
app.post('/restaurant', (req, res) => {

   console.log('Reqeust from restaurant');
   var inputData;
   req.on('data', (data) => {

     inputData = JSON.parse(data);

   });
    req.on('end', () => {
     console.log("User name : "+inputData.name);
   });

   res.write("OK!");
   res.end();

});


app.listen(3000, () => {

  console.log('Start hurry-hurry server port 3000!');

});
