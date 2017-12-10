var mysql = require('mysql');
var PDFDocument = require('pdfkit');
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

		connection.query('select *,ifnull(title,"새 회의록") title from document_list where roomNumber=?',roomNumber,function(err,result)
		{
			if(err){console.error(err);res.status(400).json({result:'1'})}
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

		var sentences = null;
		connection.query('select * from document_sentences INNER JOIN userInfo ON document_sentences.speaker=userInfo.phoneNumber where documentNumber=? order by speakTime',documentNumber,function(err,result)
		{
			if(err){console.error(err);res.status(400).json({result:'1'})}
			else
			{
				sentences = result;
				//var temp = {result:'0',data:result};
				//res.status(200).json(temp);
			}
		});
		connection.query('SELECT * FROM document_list WHERE documentNumber=?',
			documentNumber,
			function(err, result) {
				if(err) {
					console.error(err);
					res.status(400).json({'result':1})
				} else {
					res.status(200).json({'result':0, 'data':sentences, 'document':result[0]});
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
			if(err){console.error(err);res.status(400).json({result:'1'})}
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

			if(err){console.error(err);res.status(400).json({result:'1'})}
			else
			{
				connection.query('update roomList set isStart=1, documentNumber=? where roomNumber=?',[returnNumber,roomNumber],function(err,result)
				{
					if(err){console.error(err);res.status(400).json({result:'1'})}
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
					if(err){console.error(err);res.status(400).json({result:'1'})}
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

		connection.query('update roomList set isStart=1, documentNumber=? where roomNumber=?',[documentNumber,roomNumber],function(err,result)
		{
			if(err){console.error(err);res.status(400).json({result:'1'})}
			else
			{
				connection.query('select * from roomList where roomNumber=?',[roomNumber],function(err,result1)
				{
					if(err){console.error(err);res.status(400).json({result:'1'})}
					else
					{
						res.status(200).json({'result':'0','data':result1});
					}
				});
			}
		});
		
		connection.query('update document_list set date=NOW() where roomNumber=? and documentNumber=?',[roomNumber,documentNumber],function(err,result)
		{
			if(err){console.error(err);res.status(400).json({result:'1'})}
		});

		connection.query('select token from userInfo where phoneNumber IN(select phoneNumber from room_participate_List where roomNumber = ?)',roomNumber,function(err,result){
			var roomChief1;
			var documentNumber1;


			if(err){console.error(err);res.status(400).json({result:'1'})} 
			else
			{
				connection.query('select * from roomList where roomNumber=?',roomNumber,function(err,result1)
				{
					if(err){console.error(err);res.status(400).json({result:'1'})}
					else
					{
						roomChief1=result1[0].chiefPhoneNumber;
						documentNumber1=documentNumber;
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
			if(err){console.error(err);res.status(400).json({result:'1'})}
			else
			{
				res.status(200).json({result:'0'});
			}
		});
		connection.query('select token from userInfo where phoneNumber IN(select phoneNumber from room_participate_List where roomNumber = ?)',roomNumber,function(err,result){
                        var roomChief1;
                        var documentNumber1;


                        if(err){console.error(err);res.status(400).json({result:'1'})}
                        else
                        {

				connection.query('select * from roomList where roomNumber=?',roomNumber,function(err,result1)
                                {
                                        if(err){console.error(err);res.status(400).json({result:'1'})}
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
			
                        if(err){console.error(err);res.status(400).json({result:'1'})}
                        else
                        {

                                connection.query('select *, (select name from userInfo where phoneNumber=?) as speakerName from roomList where roomNumber=?',[speaker, roomNumber],function(err,result1)
                                {
                                        if(err){console.error(err);res.status(400).json({result:'1'})}
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
			if(err){console.error(err);res.status(400).json({result:'1'})}
			else
			{
				res.status(200).json({result:'0'});
			}
		});
		
		connection.query('select token from userInfo where phoneNumber IN(select phoneNumber from room_participate_List where roomNumber = ?)',roomNumber,function(err,result){

                        var roomChief1;
			var roomTitle;
			var speakerName;

                        if(err){console.error(err);res.status(400).json({result:'1'})}
                        else
                        {

                                connection.query('select *,(select name from userInfo where phoneNumber=?) as speakerName from roomList where roomNumber=?',[speaker,roomNumber],function(err,result1)
                                {
                                        if(err){console.error(err);res.status(400).json({result:'1'})}
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
			if(err){console.error(err);res.status(400).json({result:'1'})}
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

			if(err){console.error(err);res.status(400).json({result:'1'})}
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
						console.error(err);res.status(200).json({result:'1'})
					}
					res.status(200).json({'result':'0','data':stdout});
					});

			}

			});

	});


	app.get('/downloadSummary',function(req,res)
	{
		var documentNumber = req.param('documentNumber');

		fs.stat('./summary/results/'+documentNumber+'.docx',function(err,stat)
		{
			if(err==null)
			{
			res.download('./summary/results/'+documentNumber+'.docx');
			}
			else
			{
			res.status(400).json({result:'1'});
			}
		});

	});

	app.post('/loadSummary',function(req,res)
	{
		var documentNumber = req.body.documentNumber;

		fs.stat('./summary/save/'+documentNumber+'.txt',function(err,stat)
		{
			if(err==null)
			{
				var data = fs.readFileSync('./summary/save/'+documentNumber+'.txt','utf8');
				res.status(200).json({'result':0, 'data':JSON.parse(data)});
			}
			else {
				console.error(err);
				res.status(200).json({result:'1'});
			}

		});
	});


	app.post('/saveSummary',function(req,res)
	{
		var division = req.body.division;
		var writer = req.body.writer;
		var roomNumber = req.body.roomNumber;
		var documentNumber = req.body.documentNumber;
		var joiners = [];
		var subject = req.body.subject;
		var content = JSON.parse(req.body.content);


		var file = './summary/'+documentNumber+'.txt';
		fs.writeFileSync('./summary/save/'+documentNumber+'.txt',JSON.stringify(req.body),'utf8');
		connection.query('select * from userInfo INNER JOIN room_participate_List On userInfo.phoneNumber=room_participate_List.phoneNumber where roomNumber=?',roomNumber,function(err,result)
		{
			if(err){console.error(err);req.status(400).json({result:'1'})}
			else
			{
				for(var i=0;i<result.length;i++)
				{
					console.log(result[i].name);
					joiners.push(result[i].name);
				}

				var date = new Date();
				var year = date.getFullYear();
				var month = date.getMonth()+1;
				var day = date.getDate();
				var hour = date.getHours();
				var min = date.getMinutes();

				var data = year+"년"+month+"월"+day+"일"+hour+"시"+min+"분\n";
				data+=division+"\n";
				data+=writer+"\n";
				
				for(var i=0;i<joiners.length;i++)
					data+=joiners[i]+' ';

				data+="\n";

				data+=subject+"\n";

				for(var i=0;i<content.length;i++)
				{
					var item = content[i].item;
					var sentences = content[i].sentences;
					data+="itemname\n";
					data+=item+"\n";
					for(var j=0;j<sentences.length;j++)
					{
						data+="sentence\n";
						data+=sentences[j].sentence+"\n";
					}

				}
				data+="--End";

				fs.writeFileSync(file,data,'utf8');

				exec('python3 ./summary/summaryDocument.py '+documentNumber,function(err,stdout,stderr)
				{
					if(err){console.error(err);res.status(400).json({result:'1'})}

					res.status(200).json({result:'0'})
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
			var file = './document/'+filename+'.'+type;
			var titleSize = 35;
			var textSize = 10;
			var dateSize = 7;

			if(err){console.error(err);res.status(400).json({result:'1'})}
			else
			{
				var numRows = result.length;
				
				if(type=='txt')
				{
					var data = documentTitle+'\n\n';
					for(var i=0;i<numRows;i++)
					{
						data+= result[i].name+':'+result[i].sentence+'('+result[i].speakTime+')'+'\n';
					}
					fs.writeFileSync(file,data,'utf8');
					res.download(file);
				}
				else if(type=='doc')
				{
					exec('libreoffice --invisible --convert-to '+type+' --outdir ./document ./document/'+filename+'.txt',function(err,stdout,stderr)
					{
						if(err){console.error(err);res.status(400).json({result:'1'})}

						res.download(file);
					});
				}
				else if(type=='pdf')
				{
					var doc = new PDFDocument();
					var stream=doc.pipe(fs.createWriteStream(file));
					doc.font('fonts/BMJUA_ttf.ttf');
					doc.fontSize(titleSize);
					if(documentTitle==null)
						documentTitle='새 회의록';
					doc.text(documentTitle+'\n\n',{align:'center'});
					for (var i=0;i<numRows;i++)
					{
						doc.fontSize(textSize);
						doc.text(result[i].name+':'+result[i].sentence);
						doc.fontSize(dateSize);
						doc.text('('+result[i].speakTime+')');
						doc.moveDown();
					}
					doc.end();

					stream.on('finish',function()
					{
						res.download(file);
					});
				}
			}


		});
			}
		});
	});

}
