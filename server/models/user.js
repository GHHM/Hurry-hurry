var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var userSchema = new Schema({
  MEMBER_ID: String,
  FOOD_NAME: String,
  RFID_ID: String,
  ARRIVAL_TIME: String,
  APPROVAL_TIME: String,
  PROCESS_1: String,
  PROCESS_2: String,
  PROCESS_3: String,
});

// model을 user로 만들면 특별한 이름을 지정하지 않으면
// mongoDB에서 알아서 Collection name을 알아서 복수형으로 해줍니다
// 그리하여 Collection name은 users로 됩니다
module.exports = mongoose.model('user', userSchema);
