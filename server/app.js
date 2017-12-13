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
//사용 안함
app.get('/', (req, res) => {
});

//show user data
//require input: MEMBER_ID
app.post('/post/show',(req,res)=>{
	console.log('Show customer info ');
	var inputData;
	req.on('data',(data)=>{
		inputData = JSON.parse(data);
	})
	req.on('end',()=>{
		console.log(User.find({MEMBER_ID:inputData.MEMBER_ID}));
	})

})

// register
//Http connection with customer
app.post('/post/reg', (req, res) => {
	const user = new User(  {MEMBER_ID: 'init',
	  FOOD_NAME: 'init',
	  RFID_ID: 'init',
	  ARRIVAL_TIME: 'init',
	  APPROVAL_TIME: 'init',
	 PROCESS_1: 'init',
	  PROCESS_2: 'init',
	  PROCESS_3: 'init'});

   console.log('Request from costumer');
   var inputData;
   req.on('data', (data) => {
     inputData = JSON.parse(data);
   });
    req.on('end', () => {
		console.log(inputData.MEMBER_ID + " "+ inputData.FOOD_NAME+" "+inputData.RFID_ID+" "+inputData.ARRIVAL_TIME+" "+inputData.APPROVAL_TIME+" "+inputData.PROCESS_1+" "+inputData.PROCESS_2+" "+inputData.PROCESS_3);

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

   res.end();
});

//update user inputData
app.post('/post/update', (req, res) => {

});


module.exports = router;


app.listen(3000, () => {

  console.log('Start hurry-hurry server port 3000!');

});
