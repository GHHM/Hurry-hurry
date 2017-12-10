var express = require("express");
var app = express();
var bodyParser = require('body-parser');
var admin = require('firebase-admin');

var serviceAccount = require("./serviceAccountKey.json");
admin.initializeApp({
	credential: admin.credential.cert(serviceAccount),
	databaseURL: "https://snlu-e6627.firebaseio.com"
});


app.use(bodyParser.urlencoded({ extended: false}));
app.use(bodyParser.json());

var router = require('./router/userRouter')(app);
var router2 = require('./router/documentRouter')(app);
app.set('views',__dirname+'/view');
app.set('view engine', 'ejs');
app.engine('html', require('ejs').renderFile);


var server = app.listen(8000,function()
{
	console.log("Express server has started on port 8000");
})
