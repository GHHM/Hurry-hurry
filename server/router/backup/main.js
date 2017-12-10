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
		 connection.query('update userInfo set token =? where phoneNumber = ?',[token,phoneNumber],function(err,result)
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
		connection.query('insert into userInfo(name,phoneNumber) values(?,?)',[name,phoneNumber],function(err,result)
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
			if(err){console.error(err);res.status(400).json({result:
'1'})}
			else
			{
				connection.query('insert into room_participate_List(phoneNumber,roomNumber) values(?,?)',[phoneNumber,result.insertId],function(err,result1){});
				connection.query('select LAST_INSERT_ID() from roomList', function(err, resultID) {
					res.status(200).json({result:'0', 'roomNumber':resultID});					
				});
			}
		});
	});

	app.post('/roomList',function(req,res)
	{
		var phoneNumber = req.body.phoneNumber;
		connection.query('select * from roomList INNER JOIN room_participate_List ON roomList.roomNumber=room_participate_List.roomNumber where phoneNumber=?',phoneNumber,function(err,result)
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

			connection.query('select phoneNumber,name from userInfo where phoneNumber=?',phoneNumber,function(err,result)
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
				var temp = {result:'0',data:result};
				res.status(200).json(temp);
			}
		});

	});

	app.post('/documentList',function(req,res)
	{
		var roomNumber = req.body.roomNumber;

		connection.query('select * from document_list where roomNumber=?',roomNumber,function(err,result)
		{
			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
			{
				var temp = {result:'0',data:result};
				res.status(200).json(temp);
			}
		});
	});

	
	app.post('/showDocument',function(req,res)
	{
		var documentNumber = req.body.documentNumber;

		connection.query('select * from document_sentences INNER JOIN userInfo ON document_sentences.speaker=userInfo.phoneNumber where documentNumber=? order by speakTime',documentNumber,function(err,result)
		{
			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
			{
				var temp = {result:'0',data:result};
				res.status(200).json(temp);
			}
		});
	});
	
	app.post('/start',function(req,res)
	{
		var roomNumber= req.body.roomNumber;
		
		connection.query('insert into document_list(roomNumber) values(?)',roomNumber,function(err,result)
		{
			var returnNumber=result.insertId;

			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
			{
				connection.query('update roomList set isStart=1, documentNumber=? where roomNumber=?',[returnNumber,roomNumber],function(err,result)
				{
					if(err){console.error(err);req.status(400).json({result:'1'})}
					else
					{
						var temp = {result:'0',documentNumber:returnNumber};
						res.status(200).json(temp);
					}
				});
			}
		});
//////////////////////////////////////
		 connection.query('select token from userInfo where phoneNumber IN(select phoneNumber from room_participate_List where roomNumber = ?)',roomNumber,function(err,result){
			var roomChief1;
			var documentNumber1;


			if(err){console.error(err);req.status(400).json({result:'1'})} 
			else
			{
				connection.query('select * from roomList where roomNumber=?',roomNumber,function(err,result1)
				{
					if(err){console.error(err);req.status(400).json({result:'1'})}
					else
					{
						roomChief1=result1[0].chiefPhoneNumber;
						documentNumber1=result1[0].documentNumber;
					}
				

					var temp = new Array();
		
					for(var i =0;i<result.length;i++){
						temp[i] =  result[i].token;
					}
					var message = 
					{
							registration_ids : temp,
				
        						notification:
		        				{
								tag : '1',
        		        				title: '회의가 시작되었습니다',
								click_action: 'OPEN_CONFERENCE',
								icon: 'icon_conference',
								sound : 'default'
        						},
        						data:
        						{
								code: '01',
								documentNumber: documentNumber1,
								roomChief : roomChief1,
								'roomNumber' : roomNumber
							}
					};

					function checkErr(err,response)
					{
					        if(err) console.log("somethings has gone wrong!");
					        else    console.log("Successfully sent");
					        console.log(err);
					}
						fcm.send(message,checkErr);
				});

			}});
		////////////////////////message
	});

	app.post('/end',function(req,res)
	{
		var roomNumber = req.body.roomNumber;
		connection.query('update roomList set isStart=0 where roomNumber=?',roomNumber,function(err,result)
		{
			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
			{
				res.status(200).json({result:'0'});
			}
		});
		connection.query('select token from userInfo where phoneNumber IN(select phoneNumber from room_participate_List where roomNumber = ?)',roomNumber,function(err,result){
                        var roomChief1;
                        var documentNumber1;


                        if(err){console.error(err);req.status(400).json({result:'1'})}
                        else
                        {

				connection.query('select * from roomList where roomNumber=?',roomNumber,function(err,result1)
                                {
                                        if(err){console.error(err);req.status(400).json({result:'1'})}
                                        else
                                        {
                                                roomChief1=result1[0].chiefPhoneNumber;
                                                title=result1[0].title;
                                        }


                                        var temp = new Array();

                                        for(var i =0;i<result.length;i++){
                                                temp[i] =  result[i].token;
                                        }
                                        var message =
                                        {
                                                        registration_ids : temp,

                                                        notification:
                                                        {
								tag : '1',
                                                                title: '회의가 종료되었습니다',
                                                                click_action: 'OPEN_ROOM',
                                                                icon: 'icon_conference',
								sound: 'default'
                                                        },
                                                        data:
                                                        {
                                                                code: '02',
                                                                documentNumber: documentNumber1,
                                                                roomChief : roomChief1,
								'roomNumber' : roomNumber,
                                                                'roomTitle' : title
                                                        }
                                        };

                                        function checkErr(err,response)
                                        {
                                                if(err) console.log("somethings has gone wrong!");
                                                else    console.log("Successfully sent");
                                                console.log(err);
                                        }
                                                fcm.send(message,checkErr);
                                });
		}});
	});


	app.post('/sayStart',function(req,res)
        {
                var roomNumber = req.body.roomNumber;
		var documentNumber = req.body.documentNumber;
                var speaker = req.body.speaker;
           	
		connection.query('select token from userInfo where phoneNumber IN(select phoneNumber from room_participate_List where roomNumber = ?)',roomNumber,function(err,result){

                        var roomChief1;
			
                        if(err){console.error(err);req.status(400).json({result:'1'})}
                        else
                        {

                                connection.query('select *, (select name from userInfo where phoneNumber=?) as speakerName from roomList where roomNumber=?',[speaker, roomNumber],function(err,result1)
                                {
                                        if(err){console.error(err);req.status(400).json({result:'1'})}
                                        else
                                        {
						 roomChief1=result1[0].chiefPhoneNumber;
						
                                        }


                                        var temp = new Array();

                                        for(var i =0;i<result.length;i++){
                                                temp[i] =  result[i].token;
                                        }
                                        var message =
                                        {
                                                        registration_ids : temp,

                                                        notification:
                                                        {
								tag : '1',
                                                                title: '발언을 시작해따',
                                                                click_action: 'OPEN_CONFERENCE',
                                                                icon: 'icon_conference'
                                                        },
                                                        data:
                                                        {
                                                                code: '03',
                                                                'documentNumber': documentNumber,
                                                                roomChief : roomChief1,
                                                                'roomNumber' : roomNumber,
                                                                'speakerPhoneNumber' : speaker,
								'speakerName' : result1[0].speakerName
                                                        }
                                        };

                                        function checkErr(err,response)
                                        {
                                                if(err) console.log("somethings has gone wrong!");
                                                else    console.log("Successfully sent");
                                                console.log(err);
                                        }
                                                fcm.send(message,checkErr);
                                });
                }});

				 res.status(200).json({result:'0'});
        });



	app.post('/sayEnd',function(req,res)
	{
		var roomNumber = req.body.roomNumber;
		var documentNumber = req.body.documentNumber;
		var speaker = req.body.speaker;
		var speakTime = req.body.speakTime;
		var sentence = req.body.sentence;

		connection.query('insert into document_sentences(documentNumber,speaker,speakTime,sentence) values(?,?,?,?)',[documentNumber,speaker,speakTime,sentence],function(err,result)
		{
			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
			{
				res.status(200).json({result:'0'});
			}
		});
		
		connection.query('select token from userInfo where phoneNumber IN(select phoneNumber from room_participate_List where roomNumber = ?)',roomNumber,function(err,result){

                        var roomChief1;
			var roomTitle;
			var speakerName;

                        if(err){console.error(err);req.status(400).json({result:'1'})}
                        else
                        {

                                connection.query('select *,(select name from userInfo where phoneNumber=?) as speakerName from roomList where roomNumber=?',[speaker,roomNumber],function(err,result1)
                                {
                                        if(err){console.error(err);req.status(400).json({result:'1'})}
                                        else
                                        {
                                                roomChief1=result1[0].chiefPhoneNumber;
						roomTitle=result1[0].title;
						speakerName=result1[0].speakerName;	

                                        }


                                        var temp = new Array();

                                        for(var i =0;i<result.length;i++){
                                                temp[i] =  result[i].token;
                                        }
                                        var message =
                                        {
                                                        registration_ids : temp,

                                                        notification:
                                                        {
								tag : '1',
                                                                title: '발언 종료',
                                                                click_action: 'OPEN_CONFERENCE',
                                                                icon: 'icon_conference'
                                                        },
                                                        data:
                                                        {
                                                                code: '04',
                                                                'documentNumber': documentNumber,
                                                                roomChief : roomChief1,
                                                                'roomNumber' : roomNumber,
                                                                'speakerPhoneNumber' : speaker,
								'roomTitle' : roomTitle,
								'sentence' : sentence,
								'speakerName' : speakerName,
								'speakTime' : speakTime
                                                        }
                                        };

                                        function checkErr(err,response)
                                        {
                                                if(err) console.log("somethings has gone wrong!");
                                                else    console.log("Successfully sent");
                                                console.log(err);
                                        }
                                                fcm.send(message,checkErr);
                                });
                }});

	});

	app.post('/modifySentence',function(req,res)
	{
		var documentNumber = req.body.documentNumber;
		var speakTime = req.body.speakTime;
		var sentence = req.body.sentence;

		connection.query('update document_sentences set sentence=? where documentNumber=? and speakTime=?',[sentence,documentNumber,speakTime],function(err,result){
			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
				 res.status(200).json({result:'0'});
		});
	});

	
	app.post('/analyze',function(req,res)
	{
		var documentNumber =req.body.documentNumber;


		connection.query('select * from document_sentences INNER JOIN userInfo ON document_sentences.speaker = userInfo.phoneNumber where documentNumber=?',documentNumber,function(err,result)
			{
				var filename = documentNumber+'.txt';
				var file = './analyzedata/'+filename;

			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
			{
				var numRows = result.length;
				var data='';
				for(var i=0;i<numRows;i++)
				{
					data+= result[i].sentence+'\n';
				}
				fs.writeFileSync(file,data,'utf8');


				exec('python3 ./python/Twitter.py ./analyzedata/'+filename,function(err,stdout,stderr)
					{
					if(err){console.error(err);req.status(400).json({result:'1'})}
					
					res.send(stdout);
					});

			}

			});

	});

	app.get('/downloadDocument',function(req,res)
	{
		var documentNumber = req.param('documentNumber');

		var documentType =req.param('documentType');
		var type='';
		switch(documentType)
		{
			case '1':
				type='txt';
				break;
			case '2':
				type = 'doc';
				break;
			case '3':
				type ='pdf';
				break;
		}
		connection.query('select * from document_sentences INNER JOIN userInfo ON document_sentences.speaker = userInfo.phoneNumber where documentNumber=? order by speakTime',documentNumber,function(err,result)
		{
			var filename = documentNumber;
			var file = './document/'+filename+'.txt';

			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
			{
				var numRows = result.length;
				
				var data='\n';
	                        for(var i=0;i<numRows;i++)
        	                {
                	              data += result[i].name+':'+result[i].sentence+'('+result[i].speakTime+')'+'\n';
                       		}
				fs.writeFileSync(file,data,'utf8');

				
				filename=documentNumber+'.'+type;
				if(type=='txt')
					res.download(file);
				else
				{

				exec('libreoffice --invisible --convert-to '+type+' --outdir ./document '+file,function(err,stdout,stderr)
					{
						if(err){console.error(err);req.status(400).json({result:'1'})}

						res.download('./document/'+filename);
					});
				}
				
			}


		});
	});

}
