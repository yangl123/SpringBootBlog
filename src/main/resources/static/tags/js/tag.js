function Tag(inputId){
	var obj = new Object();
	if(inputId==null||inputId==""){
		alert("初始化失败，请检查参数！");
		return;
	}
	obj.inputId = inputId;
	//初始化
	obj = (function(obj){
		obj.tagValue="";
		obj.isDisable = false;
		return obj;
	})(obj);
	
	//初始化界面
	obj.initView=function(){
		var inputObj = $("#"+this.inputId);
		var inputId = this.inputId;
		inputObj.css("display","none");
		var appendStr='';
		appendStr+='<div class="tagsContaine" id="'+inputId+'_tagcontaine">';
		appendStr+='<div class="tagList"></div><input type="text" placeholder="请输入标签,按ctrl确认" class="tagInput"  onfocus="dktj()"/>';
		appendStr+='</div>';
		inputObj.after(appendStr);
		var tagInput = $("#"+inputId+"_tagcontaine .tagInput");
		if(!this.isDisable){
			$("#"+inputId+"_tagcontaine").attr("ds","1");
			tagInput.keydown(function(event){
				if(event.keyCode==13){
			         var inputValue = $(this).val();
			         tagTake.setInputValue(inputId,inputValue);
			         $(this).val("");
			    }
			});
		}else{
			$("#"+inputId+"_tagcontaine").attr("ds","0");
			tagInput.remove();
		}
		if(this.tagValue!=null&&this.tagValue!=""){
			tagTake.setInputValue(inputId,this.tagValue);
			if(this.isDisable){
				$("#"+inputId+"_tagcontaine .tagList .tagItem .delete").remove();
			}
		}
	}
	obj.disableFun=function(){
		if(this.isDisable){
			return;
		}
		var inputId = this.inputId;
		var tagInput = $("#"+inputId+"_tagcontaine .tagInput");
		tagInput.remove();
		this.isDisable = true;
		$("#"+inputId+"_tagcontaine").attr("ds","0");
		$("#"+inputId+"_tagcontaine .tagList .tagItem .delete").remove();
		tagTake.initTagEvent(inputId);
		
	}
	obj.unDisableFun = function(){
		if(!this.isDisable){
			return;
		}
		var inputId = this.inputId;
		var tagContaine = $("#"+inputId+"_tagcontaine");
		tagContaine.append('<input type="text" class="tagInput"/>');
		this.isDisable = false;
		$("#"+inputId+"_tagcontaine").attr("ds","1");
		var tagInput = $("#"+inputId+"_tagcontaine .tagInput");
		tagInput.keydown(function(event){
				if(event.keyCode==13){
			         var inputValue = $(this).val();
			         tagTake.setInputValue(inputId,inputValue);
			         $(this).val("");
			    }
		});
		$("#"+inputId+"_tagcontaine .tagList .tagItem").append('<div class="delete"></div>');
		tagTake.initTagEvent(inputId);
		
	}
	obj.getValues=function () {

		return values.substring(0,values.length-1);
    }
	return obj;
}

var tagTake ={

	"setInputValue":function(inputId,inputValue){
		if(inputValue==null||inputValue==""){
			return;
		}
		var tagListContaine = $("#"+inputId+"_tagcontaine .tagList");
		inputValue = inputValue.replace(/，/g,",");
		var inputValueArray = inputValue.split(",");
		for(var i=0;i<inputValueArray.length;i++){
			var valueItem = $.trim(inputValueArray[i]);
			if(valueItem!=""){
				var appendListItem = tagTake.getTagItemModel(valueItem);
				tagListContaine.append(appendListItem);
			}
		}
		tagTake.resetTagValue(inputId);
		tagTake.initTagEvent(inputId);
	},
	"initTagEvent":function(inputId){
		$("#"+inputId+"_tagcontaine .tagList .tagItem .delete").off();
		$("#"+inputId+"_tagcontaine .tagList .tagItem").off();
		var ds =  $("#"+inputId+"_tagcontaine").attr("ds");
		if(ds=="0"){
			return;
		}
		$("#"+inputId+"_tagcontaine .tagList .tagItem .delete").mousedown(function(){
			$(this).parent().remove();
			tagTake.resetTagValue(inputId);
		});
		
		$("#"+inputId+"_tagcontaine .tagList .tagItem").dblclick(function(){
			var tagItemObj = $(this);
			 $(this).css("display","none");
			var updateInputObj = $("<input type='text' class='updateInput' value='"+tagItemObj.find("span").html()+"'>");
			updateInputObj.insertAfter(this);
			updateInputObj.focus();
			updateInputObj.blur(function(){
				var inputValue = $(this).val();
				if(inputValue!=null&&inputValue!=""){
					tagItemObj.find("span").html(inputValue);
					tagItemObj.css("display","block");
				}else{
					tagItemObj.remove();
				}
				updateInputObj.remove();
				tagTake.resetTagValue(inputId);
			});
			updateInputObj.keydown(function(event){

				if(event.keyCode==13){
			        var inputValue = $(this).val();
					if(inputValue!=null&&inputValue!=""){
						tagItemObj.find("span").html(inputValue);
						tagItemObj.css("display","block");
					}else{
						tagItemObj.remove();
					}
					updateInputObj.remove();
					tagTake.resetTagValue(inputId);
			    }
			});
		});
	},
	"resetTagValue":function(inputId){
		var tags = $("#"+inputId+"_tagcontaine .tagList .tagItem");
		var tagsStr="";
		for(var i=0;i<tags.length;i++){
			tagsStr+=tags.eq(i).find("span").html()+",";
		}
		tagsStr = tagsStr.substr(0,tagsStr.length-1);
		$("#"+inputId).val(tagsStr);
        var values="";
        var inputId = this.inputId;
        var tagListContaine = $("#"+inputId+"_tagcontaine .tagList");
        $(".tagItem").each(function () {
            values+=$(this).children("span").text()+",";
        })
		$("#tages").val(values);
	},
	"getTagItemModel":function(valueStr){
		var color=getColor();
		return '<div class="tagItem" style="background-color: '+color+'"><span style="padding: 5px 5px 5px 5px">'+valueStr+'</span><div class="delete"></div></div>';
	},"testAlert":function(valueStr){
		alert("ceshi chenggong")
	}
}
var arrs=['#6fa3ef','#ff9800','#46c47c'];
function getColor(){
	var num=Math.floor(Math.random()*3);
	return arrs[num];
}

