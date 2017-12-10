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

	app.post('/modifyDocumentName',function(req,res)
	{
		var title = req.body.title;
		var roomNumber = req.body.roomNumber;
		var documentNumber = req.body.documentNumber;

		connection.query('update document_list set title=? where roomNumber=? and documentNumber=?',[title,roomNumber,documentNumber],function(err,result)
		{
			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
			{
				res.status(200).json({result:'0'})
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
        		        				title: result1[0].title+'방의 회의가 시작되었습니다',
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

	app.post('/resume',function(req,res)
	{
		var roomNumber = req.body.roomNumber;
		var documentNumber=req.body.documentNumber;

		connection.query('update roomList set isStart=1 where roomNumber=? and documentNumber=?',[roomNumber,documentNumber],function(err,result)
		{
			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
			{
				connection.query('select * from roomList where roomNumber=? and documentNumber=?',[roomNumber,documentNumber],function(err,result1)
				{
					if(err){console.error(err);req.status(400).json({result:'1'})}
					else
					{
						res.status(200).json({'result':'0','data':result1});
					}
				});
			}
		});
		
		connection.query('update document_list set date=? where roomNumber=? and documentNumber=?',[new Date().getTime,roomNumber,documentNumber],function(err,result)
		{
			if(err){console.error(err);req.status(400).json({result:'1'})}
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
        		        				title: result1[0].title+'방의 회의가 시작되었습니다',
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
                                                                title: '회의가 진행중입니다.',
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
					if(err){
						console.error(err);req.status(200).json({result:'1'})
					}
					res.status(200).json({'result':'0','data':stdout});
					});

			}

			});

	});

	app.get('/downloadDocument',function(req,res)
	{
		var documentNumber = req.param('documentNumber');

		var documentType =req.param('documentType');
		var documentTitle='';
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
		connection.query('select * from document_list where documentNumber=?',documentNumber,function(err,result1)
		{
			if(err){console.log(err);res.status(400).json({result:'1'})}
			else
			{
				documentTitle=result1[0].title;
		connection.query('select * from document_sentences INNER JOIN userInfo ON document_sentences.speaker = userInfo.phoneNumber where documentNumber=? order by speakTime',documentNumber,function(err,result)
		{
			var filename = documentNumber;
			var file = './document/'+filename+'.txt';

			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
			{
				var numRows = result.length;
				
				var data=documentTitle+'\n\n';
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
			}
		});
	});

}
