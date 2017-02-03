///<reference path="tabs/tab.ts"/>
///<reference path="tree/tree.ts"/>
declare var $;

module home{

    function updateFrameSize(){
        let heightUp = $(".content-up>div").css("height");
        $(".content-up").css("height", heightUp);

        let bodyHeight = parseInt($("body").css("height").replace("px", ""));

        let contentDownHeight = bodyHeight - parseInt(heightUp.replace("px", ""));
        $(".content-down").css("height", contentDownHeight + "px");


        let tabHeight = parseInt($("#tabNew .tab").css("height").replace("px", ""));
        $("#tabContent").css("height", (contentDownHeight - tabHeight) + "px");
        $("#tabContent-up").css("height", (contentDownHeight - tabHeight) + "px");
    }

    var myTab = new tab.Tab("tabNew");

    $(document).ready(()=>{
        updateFrameSize();
        $(".container-fluid").css("visibility", "visible");
        //myTab.triggerClickHome();
        $(window).resize(()=>{
           updateFrameSize();
        });
    });


    $(".nav-left").mCustomScrollbar({
        theme: "minimal-dark",
        scrollInertia: 0
    });


    myTab.addMore({
        id: "19",
        name: "刷新"
    });
    myTab.addMore({
        id: "20",
        name: "关闭当前页"
    });
    myTab.addMore({
        id: "21",
        name: "关闭全部页"
    });
    myTab.addMore({
        id: "22",
        name: "关闭其它页"
    });

    myTab.setHomeClickListener(function () {
        let homeFrame = $("#tabContent #home");
        if (homeFrame.length == 0){
            $("#tabContent").append('<iframe src="http://192.168.7.22/BusinessManagement" id="home" style="width:100%;height:100%" ></iframe>');
            homeFrame = $("#tabContent #home");
            $("#tabContent #home").active(()=>{
                $("body").click();
            });
            $("iframe").css("display", "none");
        }
        homeFrame.css("display", "");
        myTab.disableMore("20");
    });

    myTab.setMoreClickListener(function (data) {

        let activeTab:tab.TabInfo = myTab.getActiveTab();
        if (data.id == "19"){
           if (activeTab != undefined){
               let tabFrame = $("#tabContent #" + activeTab.id);
               $("#tabContent #" + activeTab.id)[0].src = tabFrame[0].src;
           }else{
               let homeFrame = $("#tabContent #home");
               $("#tabContent #home")[0].src = homeFrame[0].src;
           }
        } else if (data.id == "20"){
            myTab.triggerClickClose(activeTab.id);
        } else if (data.id == "21"){//close all
            let tabs = myTab.getTabs();
            let tabCopy = [];
            for(let i = 0; i < tabs.length; ++i){
                tabCopy.push(tabs[i]);
            }
            for(let i = 0; i < tabCopy.length; ++i){
                myTab.triggerClickClose(tabCopy[i].id);
            }

        } else if (data.id == "22"){//close other
            let tabs = myTab.getTabs();
            let tabCopy = [];
            for(let i = 0; i < tabs.length; ++i){
                if (myTab.getActiveTab() != tabs[i]){
                    tabCopy.push(tabs[i]);
                }
            }
            for(let i = 0; i < tabCopy.length; ++i){
                myTab.triggerClickClose(tabCopy[i].id);
            }

        }
    });

    myTab.setTabClickListener(function (data:any) {
        myTab.enableMore("20");
        $("iframe").css("display", "none");
        $("#tabContent #" + data.id).css("display", "");
    });

    myTab.setCloseTabClickListener(function (data) {
        if (data == undefined){
            $("#tabContent #home").remove();
        }else{
            $("#tabContent #" + data.id).remove();
        }
    });

    myTab.triggerClickHome();

    var tree1 = new tree.Tree("tree");
    tree1.render([{
        data: {
            id: "1",
            value: "1你你你你"
        },
        subNodes: [{
            data: {
                id: "10",
                value: "10testsetstest"
            },
            subNodes: [{
                data: {
                    id: "1203",
                    value: "201testsetstest",
                    url:"http://192.168.7.22/BusinessManagement"
                }
            }, {
                data: {
                    id: "1212",
                    value: "211testsetstest",
                    url:"http://192.168.7.22/BusinessManagement"
                }
            }, {
                data: {
                    id: "1221",
                    value: "221test",
                    url:"http://192.168.7.22/BusinessManagement"
                }
            }]
        }, {
            data: {
                id: "11",
                value: "11testsetstest",
                url:"http://192.168.7.22/BusinessManagement"
            }
        }, {
            data: {
                id: "12",
                value: "12testsetstest"
            },
            subNodes: [{
                data: {
                    id: "2032",
                    value: "201testsetstest",
                    url:"http://192.168.7.22/BusinessManagement"
                }
            }, {
                data: {
                    id: "2121",
                    value: "211testsetstest",
                    url:"http://192.168.7.22/BusinessManagement"
                }
            }, {
                data: {
                    id: "2214",
                    value: "221test",
                    url:"http://192.168.7.22/BusinessManagement"
                }
            }]
        }
        ]
    }, {
        data: {
            id: "2",
            value: "2你你你你"
        },
        subNodes: [{
            data: {
                id: "20",
                value: "20testsetstest",
                url:"http://192.168.7.22/BusinessManagement"
            }
        }, {
            data: {
                id: "21",
                value: "21testsetstest"
            }
        }, {
            data: {
                id: "22",
                value: "22test"
            },
            subNodes: [{
                data: {
                    id: "203",
                    value: "201testsetstest",
                    url:"http://192.168.7.22/BusinessManagement"
                }
            }, {
                data: {
                    id: "212",
                    value: "211testsetstest",
                    url:"http://192.168.7.22/BusinessManagement"
                }
            }, {
                data: {
                    id: "221",
                    value: "221test",
                    url:"http://192.168.7.22/BusinessManagement"
                }
            }]
        }]
    }]);

    tree1.setOnClickListener(function(node:any){
        if (node.data.url != undefined){
            let tab = myTab.findTab(node.data.id + "tab");
            if (tab == undefined){
                myTab.addTab({
                    name: node.data.value,
                    id: node.data.id + "tab"
                });

                $("iframe").css("display", "none");
                let tabFrame = $("#tabContent #" + node.data.id + "tab");
                if (tabFrame.length == 0){
                    $("#tabContent").append(
                        '<iframe src="' + node.data.url + '" ' +
                        'id="' + node.data.id + "tab" + '" ' +
                        'style="width:100%;height:100%">' +
                        '</iframe>');
                    tabFrame = $("#tabContent #" + node.data.id + "tab");
                    tabFrame.active(()=>{
                        $("body").click();
                    });
                }
            }
            myTab.triggerClickTab(node.data.id + "tab");
        }

    });

}