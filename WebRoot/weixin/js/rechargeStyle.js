jQuery(function(){
    jQuery(".PackageCont .ml").click(function(){
 		if(jQuery(this).hasClass("Slt")){
    	 	jQuery(this).removeClass("Slt");
    	 	jQuery(this).children('img').hide();
            }
            else{
                jQuery(this).addClass("Slt");
                jQuery(this).children('img').show();
            }
    })

    jQuery(".PackageTit span b").click(function(){
    	 if(jQuery(this).hasClass("add")){
    	 
    	 	jQuery(".PackageTit span b").removeClass("add");
    	 	
            }
            else{
                jQuery(".PackageTit span b").addClass("add");
                
            }
    })

    var Bw=jQuery(window).width();
    var Mw=(Bw-300)/2;
    jQuery(".Mask1").css("left",Mw);

 	var Bh=jQuery(window).height();
 	var Mhi=jQuery(".Mask1").height();
    var MH=(Bh-Mhi)/2;
    jQuery(".Mask1").css("top",MH);

    var Bw1=jQuery(window).width();
    var Mw1=(Bw1-300)/2;
    jQuery(".Mask2").css("left",Mw1);

    var Bh1=jQuery(window).height();
    var Mhi1=jQuery(".Mask2").height();
    var MH1=(Bh1-Mhi1)/2;
    jQuery(".Mask2").css("top",MH1);


	jQuery(".close").click(function(){
		jQuery(".zhezhao").hide();
		jQuery(".Mask1").hide();
    	jQuery(".Mask2").hide();
        jQuery("body").css("position","relative")
	});
	
	jQuery(".btnWrap input").click(function(){
		jQuery(".zhezhao").hide();
		jQuery(".Mask1").hide();
    	jQuery(".Mask2").hide();
        jQuery("body").css("position","relative")
	});
	
})
	




   



