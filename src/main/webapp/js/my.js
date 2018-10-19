/**
 * 根据选项获取授权值
 * @param data
 */
 function getTheCheckBoxValue() {
    var test = $("input[name='sProblem']:checked");
    var checkBoxValue = "";
    var iAuth = 0;
    test.each(function () {
        var value = $(this).val();
        if(value.indexOf("office") != -1 && value.indexOf("annotation") != -1 )
        {
            iAuth = iAuth | 1;
        }else if(value.indexOf("web") != -1 && value.indexOf("annotation") != -1 ){
            iAuth = iAuth | 2;
        }else if(value.indexOf("ESSWebSign") != -1){
            iAuth = iAuth | 4;
        }else if(value.indexOf("ESSWordSign") != -1){
            iAuth = iAuth | 8;
        }else if(value.indexOf("ESSExcelSign") != -1){
            iAuth = iAuth | 16;
        }else if(value.indexOf("ESSPdfSign") != -1){
            iAuth = iAuth | 32;
        }else if(value.indexOf("ESSMidWare") != -1){
            iAuth = iAuth | 64;
        }else{

        }
    });
    return iAuth;
}

/**
 * 检查输入值是否为空
 * @param data
 * @returns {boolean}
 */
function isNull(val) {
    if (val == '' || val == undefined || val == null) {
        //空
        return true;
    } else {
        // 非空
        return false;
    }
}

function GetProductInfoFromAuthNumber(iAuth){
    var sRet = "";
    if((iAuth & 1) != 0){
        sRet = sRet + "office annotation@" ;
    }
    if((iAuth & 2) != 0){
        sRet = sRet + "web annotation@" ;
    }
    if((iAuth & 4) != 0){
        sRet = sRet + "ESSWebSign@" ;
    }
    if((iAuth & 8) != 0){
        sRet = sRet + "ESSWordSign@" ;
    }
    if((iAuth & 16) != 0){
        sRet = sRet + "ESSExcelSign@" ;
    }
    if((iAuth & 32) != 0){
        sRet = sRet + "ESSPdfSign@" ;
    }
    if((iAuth & 64) != 0){
        sRet = sRet + "ESSMidWare@" ;
    }
    return sRet;
}


//对时间进行年的加减操作
function dateOperator(date,years) {
    date = date.replace(/-/g,"/"); //更改日期格式
    var nd = new Date(date);

    nd.setFullYear(nd.getFullYear()+parseInt(years));

    var y = nd.getFullYear();
    var m = nd.getMonth()+1;
    var d = nd.getDate();
    if(m <= 9) m = "0"+m;
    if(d <= 9) d = "0"+d;
    var cdate = y+"-"+m+"-"+d;
    return cdate;
}

/**
 * @param str 需要操作的字符串
 * @param str1 需要替换的字符串
 * @param str2 替换结果字符串
 *
 */
function replaceAll(str,str1,str2) {

    re = new RegExp(str1,"g"); //定义正则表达式
    //第一个参数是要替换掉的内容，第二个参数"g"表示替换全部（global）。
    var Newstr = str.replace(re, str2); //第一个参数是正则表达式。
    //本例会将全部匹配项替换为第二个参数。
    return Newstr;
}