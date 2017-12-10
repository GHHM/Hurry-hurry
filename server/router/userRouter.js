var mysql = require('mysql');
var connection = mysql.createConnection(
{
	host : 'localhost',
	port : 1433,
	user : 'root',
	password : 'rkddbsghd',
	socketPath : '/var/run/mysqld/mysqld.sock',
	database : 'SNLU',
	dateStrings : true
});

	var FCM = require('fcm-node');

        var serverkey = 'AAAAeDG3sAY:APA91bGEWWFkFyG2JF5Goum8KdBG6NV4RfqWnS9UK_B_cLm2NI7zIW2fT8LAHJUXHD9ZKBlPm1COTDxzEjoNpyA_RGkFouRA8XrIqj-LjczA1JHh5sfg7Dt7T7xS-TUvuyWSb6oBIlKKqn0qlKEXud9Rhg4zEGBkgg';
        var fcm = new FCM(serverkey);

	var fs = require('fs');

	var exec = require('child_process').exec;


module.exports = function(app)

{
	app.get('/',function(req,res)
	{
		res.render('index.html');
	});
	app.post('/dong',function(req,res)
	{	
		var token = req.body.token;
		var phoneNumber = req.body.phoneNumber;
		var imageurl = req.body.imageurl;
		 connection.query('update userInfo set token =?,imageurl=? where phoneNumber = ?',[token,imageurl,phoneNumber],function(err,result)
		{
			if(err){console.error(err);res.status(400).json({result:'1'})}
			else
				res.status(200).json({result:'0'});
			});
	});
	app.get('/truncate',function(req,res)
	{
		connection.query('set foreign_key_checks=0',function(err,result)
		{
		});
		
		connection.query('truncate document_list',function(err,result)
                {
                });

		connection.query('truncate document_sentences',function(err,result)
                {
                });
		connection.query('truncate roomList',function(err,result)
                {
                });
		connection.query('truncate room_participate_List',function(err,result)
                {
                });
		connection.query('truncate userInfo',function(err,result)
                {
                });

		connection.query('set foreign_key_checks=1',function(err,result)
                {
                });
		res.status(200).send('asdgasdg');

	});


	app.post('/about',function(req,res)
	{
		req.accepts('application/json');

		console.log(req.body);
	});
	app.post('/join',function(req,res)
	{
		var name = req.body.name;
		var phoneNumber = req.body.phoneNumber;
		var imageurl = req.body.imageurl;
		connection.query('insert into userInfo(name,phoneNumber,imageurl) values(?,?,?)',[name,phoneNumber,imageurl],function(err,result)
		{
			if(err){console.error(err);res.status(200).json({result:'1'})}
			else
				res.status(200).json({result:'0'});
		});
	});
	app.post('/isDuplicate',function(req,res)
	{
		var phoneNumber = req.body.phoneNumber;
		connection.query('select count(*) as result from userInfo where phoneNumber = ?',phoneNumber,function(err,result)
		{
			if(err){console.error(err);res.status(400).json({result:'1'})}
			else
				res.status(200).json(result[0]);
		});
	});


	app.post('/roomAdd',function(req,res)
	{
		var phoneNumber = req.body.phoneNumber;
		var title = req.body.title;
		
		connection.query('insert into roomList(title,chiefPhoneNumber) values(?,?)',[title,phoneNumber],function(err,result)
		{
			if(err){console.error(err);res.status(400).json({result:'1'});}
			else
			{
				connection.query('insert into room_participate_List(phoneNumber,roomNumber) values(?,?)',[phoneNumber,result.insertId],function(err,result1){});
				connection.query('select LAST_INSERT_ID() as roomNumber', function(err,resultID) {
					res.status(200).json({result:'0', 'roomNumber':resultID[0].roomNumber});
				});
			}
		});
	});
	
	app.post('/documentDelete',function(req,res)
	{
		var roomNumber = req.body.roomNumber;
		var documentNumber = req.body.documentNumber;
		connection.query('set foreign_key_checks=0',function(err,result)
		{
			connection.query('delete from document_list where roomNumber=? and documentNumber=?',[roomNumber, documentNumber],function(err,result)
			{
				if(err){console.error(err);res.status(400).json({result:'1'})}
				else
				{
					connection.query('delete from document_sentences where documentNumber=?',documentNumber,function(err,result)
					{
						if(err){console.error(err);res.status(400).json({result:'1'})}
						
						else
						{
							connection.query('set foreign_key_checks=1',function(err,result)
							{
								if(err){console.error(err);res.status(400).json({reseult:'1'})}
								res.status(200).json({result:'0'});
							});
						}
					});
				}
			});
		});
	});

	app.post('/roomDelete',function(req,res)
	{
		var roomNumber = req.body.roomNumber;

		connection.query('set foreign_key_checks=0',function(err,result)
		{
			connection.query('delete from roomList where roomNumber=?',roomNumber,function(err,result)
			{
				if(err){console.error(err);res.status(400).json({result:'1'});}
				else
				{
					connection.query('delete from room_participate_List where roomNumber=?',roomNumber,function(err,result)
					{
						if(err){console.error(err);res.status(400).json({result:'1'});}
						
						else
						{
							connection.query('set foreign_key_checks=1',function(err,result)
							{
								if(err){console.error(err);res.status(400).json({result:'1'});}
								res.status(200).json({result:'0'});
							});
						}
					});
				}
			});
		});
	});

	app.post('/roomList',function(req,res)
	{
		var phoneNumber = req.body.phoneNumber;
		connection.query('select *, (SELECT COUNT(*) FROM room_participate_List WHERE roomNumber=roomList.roomNumber) as count from roomList INNER JOIN room_participate_List ON roomList.roomNumber=room_participate_List.roomNumber where phoneNumber=?',phoneNumber,function(err,result)
		{
			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
			{
				var temp = {result:'0',data:result};
				res.status(200).json(temp);
			}
		});
	});
	app.post('/confirmUser',function(req,res)
		{
			var phoneNumber = req.body.phoneNumber;

			connection.query('select phoneNumber,name, imageurl from userInfo where phoneNumber=?',phoneNumber,function(err,result)
				{
					if(err){console.error(err);req.status(400).json({result:'1'})}
					else
					{
						if(result.length!=0)
							res.status(200).json({'result':'0',data:result});
						else
							res.status(200).json({result:'1'});
					}
				});
		});
	app.post('/comfirmUser2',function(req,res)
		{
			var pattern = req.body.phoneNumber;

			coneection.query('select phoneNumber,name,imageurl from userInfo where phoneNumber like ?% or name like ?%',[pattern,pattern],function(err,result)
				{
					if(err){console.error(err);req.status(400).json({result:'1'})}
					else
					{
						if(result.length!=0)
							res.status(200).json({'result':'0',data:result});
						else
							res.status(200).json({result:'1'});
					}
				});
		});
	app.post('/userAdd',function(req,res)
	{
		var roomNumber = req.body.roomNumber;
		var phoneNumbers = req.body.phoneNumbers;
		var roomTitle;
		var roomChief;
	
		for(var i=0; i<phoneNumbers.length; i++) {	
			var phoneNumber = phoneNumbers[i].phoneNumber;
			connection.query('insert into room_participate_List(roomNumber,phoneNumber) values(?,?)',[roomNumber,phoneNumber],function(err,result)
			{
				if(err){console.error(err);req.status(400).json({result:'1'})}
				else
				{
				}
			});
			connection.query('select * from roomList where roomNumber=?',roomNumber,function(err,result)
			{
				if(err)console.error(err);
				else
				{
					roomTitle=result[0].title;
					roomChief=result[0].chiefPhoneNumber;
				}
			});
			connection.query('select token from userInfo where phoneNumber=?',phoneNumbers[i].phoneNumber,function(err,result){
				var token = result[0].token;
				var message = 
				{
							to : token,
					  		notification:
                                                        {
                                                                tag : '1',
                                                                title: roomTitle+'방에 초대되었습니다.',
                                                                click_action: 'OPEN_ROOM',
                                                                icon: 'icon_conference'
                                                        },
                                                        data:
                                                        {
                                                                code: '05',
								'roomTitle' : roomTitle,
                                                                'roomChief' : roomChief,
                                                                'roomNumber' : roomNumber
                                            		}
					
                                }

				function checkErr(err,response)
                                {
					 if(err) console.log("somethings has gone wrong!");
                                         else    console.log("Successfully sent");
                           	               console.log(err);
                                }
                                fcm.send(message,checkErr);

			});
		}
	
			res.status(200).json({result:'0'});

           });


	app.post('/userDel',function(req,res)
	{
		var roomNumber = req.body.roomNumber;
		var phoneNumber = req.body.phoneNumber;

		connection.query('delete from room_participate_List where roomNumber=? and phoneNumber=?',[roomNumber,phoneNumber],function(err,result)
		{
			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
			{
				res.status(200).json({result:'0'});
			}
		});
	});
	app.post('/userList',function(req,res)
	{
		var roomNumber = req.body.roomNumber;
		
		connection.query('select * from userInfo INNER JOIN room_participate_List ON userInfo.phoneNumber=room_participate_List.phoneNumber where roomNumber=?',roomNumber,function(err,result)
		{
			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
			{
				var temp = {result:roomNumber,data:result};
				res.status(200).json(temp);
			}
		});

	});

}
