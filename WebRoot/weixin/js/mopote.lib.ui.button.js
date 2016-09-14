/**
 * @fileoverview
 * @author galle.chu@skymobi.com
 * @version 1.0
 * Created by Gallen Chu ( http://hi.baidu.com/zhuguoneng )
 */

/*
 * 2012-05-14: <version 1.0> - <galle.chu@skymobi.com> - <创建>
 * 2012-05-30: <version 1.1> - <galle.chu@skymobi.com> - <增加图片预加载>
 */

/**
 * @class
 * Button，实现按钮功能，每按钮对应一组背景图片，分别表示[0、1、3、4]四种状态。<br />
 * 应用时需要定义对象的click函数以实现其功能。<br />
 * 类的所有方法都必须在类示例化为dom对象后才能调用。<br />
 * usage:<br />
 * var buttonTest = new MOPOTE.LIB.UI.Button("buttonTest", 50, 20, "bt50", "按钮");<br />
 * buttonTest.click = function(){在这里定义按钮按下时的操作};<br />
 * buttonTest.show();<br />
 * @constructor
 * @param {string} id Button的id值
 * @param {int} w Button的宽度
 * @param {int} h Button的高度
 * @param {string} iconame_prefix Button所用背景图片前缀
 * @param {string} btext Button上的文字,无文字时使用“”表示
 * @param {boolean} bdisabled Button初始化时是否可用，flase为可用，无值是视为可用
 * @param {string} type button的input类型
 * @param {int} className Button的样式(一般定义字体类型、大小)
 * @param {string} baccesskey button快捷键
 * @param {string} bcursor button的鼠标指针（非必填项）
 * @param {string} btitle button的tips（非必填项）
 */
MOPOTE.LIB.UI.Button = function(bid, w, h, iconame_prefix, btext, bdisabled, type, className, baccesskey, bcursor, btitle){
	this.id = bid;
	this.text = btext;
	this.actived = false;
	this.disabled = bdisabled||false;
	this.display = 'inline';
	this.accesskey = baccesskey;
	this.cursor = (!bcursor||bcursor=='')?'pointer':bcursor;
	this.title = btitle||null;
	this.popopen = false;
	this.className = className || 'uiButtonClass';
	this.type = type || 'button';
	this.icon = {
		bt:         (iconame_prefix!='')?(iconame_prefix + '_0.png'):'',
		btOver:     (iconame_prefix!='')?(iconame_prefix + '_1.png'):'',
		btDown:     (iconame_prefix!='')?(iconame_prefix + '_3.png'):'',
		btDisable:  (iconame_prefix!='')?(iconame_prefix + '_4.png'):''
	};
	this.toString = function (){
		var str = '';
		str += '<button id="' + this.id+ '" name="' + this.id+ '" type="' + this.type+ '" vaglin="middle" ';
		//str += ' hidefocus="true"';//隐藏焦点虚线框
		str += ' style="border:0px; line-height:' + h + 'px; text-indent:0px; text-align:center; width:' + w + 'px; height:' + h + 'px; display:' + this.display + '; cursor:'+ this.cursor+'; FILTER: progid:DXImageTransform.Microsoft.Gradient(Enabled=false); ';
		if(this.disabled){
			str += 'cursor:auto; background:url(' + this.icon.btDisable + ')" disabled="true"';
		}else{
			str += 'background:url(' + this.icon.bt + ')"';
		}
		if (this.accesskey != null){
			str += ' accesskey="' + this.accesskey + '"';
		}
		if(this.title != null){
			str += ' title="' + this.title + '"';
		}
		str += '>';
		
		if (this.text != null){
		    str += this.text;
		}
		str += '</button>';
		return str;
	};

	var ___target = this;
	
	/**
 	 * 显示button
 	 * @param {object} container button的父节点，，若为空直接输出。
 	 */
	this.show = function (container){
		if(container){
			//container.innerHTML += this.toString();
			jQuery(container).append(this.toString());//依赖Jquery
		}else{
			document.write(this.toString());
		}
		if(this.className){
			$I(this.id).className = this.className;
		}

		$I(this.id).onmouseover = function(e){___target.mouseOver();if (e&&e.stopPropagation){e.stopPropagation();}else{window.event.cancelBubble = true;}};
		$I(this.id).onmousedown = function(e){___target.mouseDown();if (e&&e.stopPropagation){e.stopPropagation();}else{window.event.cancelBubble = true;}};
		$I(this.id).onmouseout = function(e){___target.mouseOut();if (e&&e.stopPropagation){e.stopPropagation();}else{window.event.cancelBubble = true;}};
		$I(this.id).onmouseup = function(e){___target.mouseUp();if (e&&e.stopPropagation){e.stopPropagation();}else{window.event.cancelBubble = true;}};
		$I(this.id).onclick = function(e){___target.click();if (e&&e.stopPropagation){e.stopPropagation();}else{window.event.cancelBubble = true;}};
	};

	/*图片预加载*/
	for(var imgSrc in this.icon){
		var wndImg = document.createElement("img"); 
		wndImg.src = "";
		wndImg.src = this.icon[imgSrc];
	}
};

/**
 * @private
 */
MOPOTE.LIB.UI.Button.prototype.mouseOver = function (){
	if (!this.disabled && !this.actived && !this.popopen){
		$I(this.id).style.backgroundImage = 'url(' + this.icon.btOver + ')';
	}
};

/**
 * @private
 */
MOPOTE.LIB.UI.Button.prototype.mouseOut = function (){
	if (!this.disabled && !this.actived && !this.popopen){
		$I(this.id).style.backgroundImage = 'url(' + this.icon.bt + ')';
	}
};

/**
 * @private
 */
MOPOTE.LIB.UI.Button.prototype.mouseDown = function (){
	var theEvent = window.event || arguments.callee.caller.arguments[0];
	if (theEvent.button == 2){
		return;
	}
	if (!this.disabled){
		$I(this.id).style.backgroundImage = 'url(' + this.icon.btDown + ')';
	}
};

/**
 * @private
 */
MOPOTE.LIB.UI.Button.prototype.mouseUp = function (){
	if (!this.disabled && !this.actived){
		$I(this.id).style.backgroundImage = 'url(' + this.icon.bt + ')';
	}
};

/**
 * 设置Button的可用状态
 * @param {boolean} d d=true:Button设为可用，反之回复初始状态
 */
MOPOTE.LIB.UI.Button.prototype.disable = function (d){
  $I(this.id).style.backgroundImage = d ? 'url(' + this.icon.btDisable + ')' : 'url(' + this.icon.bt + ')';
  if(d){
	$I(this.id).style.cursor = "auto";
  }else{
	$I(this.id).style.cursor = this.cursor;
  }

  this.disabled = d;
  this.actived = false;
  $I(this.id).disabled = d;
};

/**
 * 设置Button的激活状态
 * @param {boolean} a a=true:激活Button，反之恢复初始状态
 */
MOPOTE.LIB.UI.Button.prototype.active = function (a){
	if (this.actived == a){
		return;
	}
	$I(this.id).style.backgroundImage = a ? 'url(' + this.icon.btDown + ')' : 'url(' + this.icon.bt + ')';
	this.disabled = false;
	this.actived = a;
};

/**
 * 设置Button的tip值
 * @param {string} t tip值.
 */
MOPOTE.LIB.UI.Button.prototype.tip = function (t){
	this.title = t;
	if($I(this.id)){
		$I(this.id).title = t;
	}
};
/**
 * 设置Button的文字
 * @param {string} t Button上显示的文字，可以包含标签.
 */
MOPOTE.LIB.UI.Button.prototype.changeText = function (t){
	this.text = t;
	$I(this.id).innerHTML = t;
};
/**
 * 设置Button的显示隐藏属性
 * @param {boolean} d true时显示。
 */
MOPOTE.LIB.UI.Button.prototype.displayIt = function (d){
	this.display = d?'inline':'none';
	if($I(this.id)){
		$I(this.id).style.display = d?'inline':'none';
	}
};
/**
 * 设置Button的快捷键
 * @param {string} a 快捷键对应的字母。
 */
MOPOTE.LIB.UI.Button.prototype.changeAccesskey = function (a){
	if($I(this.id)){
		$I(this.id).accessKey = a;
	}
	this.accesskey = a;
};
/**
 * 设置Button的鼠标指针
 * @param {string} c 指针名。
 */
MOPOTE.LIB.UI.Button.prototype.changeCursor = function (c){
	if($I(this.id)){
		$I(this.id).style.cursor = c;
	}
	this.cursor = c;
};
	
/**
 * @private
 */
MOPOTE.LIB.UI.Button.prototype.click = function (){
	if (!this.disabled){
		this.clickMe();
	}
};

/**
 * 设置背景为down状态
 * @param {bool} b b==true时，设置为down状态，false时为up状态
 */
MOPOTE.LIB.UI.Button.prototype.setDown = function (b){
	this.popopen = b||false;
	if (this.popopen) {
		$I(this.id).style.backgroundImage = 'url(' + this.icon.btDown + ')';
	}
	else {
		$I(this.id).style.backgroundImage = 'url(' + this.icon.bt + ')';	
	}
};

	
/**
 * 相当于dom对象的onClick事件，由用户自定义，在鼠标点击时执行，以实现按钮功能。
 */
MOPOTE.LIB.UI.Button.prototype.clickMe = function (){};