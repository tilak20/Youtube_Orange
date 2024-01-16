javascript: (function () {
    var isClickCooling = true

    console.log("加载500px解析代码>>>>>>>>>")

    function warn(tag, msg = "") {
        console.log("warn:: " + tag + " , " + msg)
        ADAPTATION_HOLDER.warn(tag, msg);
    }

    function receiveJsParseResult(jsonResult) {
        console.log("receiveJsParseResult:: " + jsonResult)
        ADAPTATION_HOLDER.receiveJsParseResult(jsonResult);
    }

    var tagMedia = function (e, imgUrl) {
        var targetContainer = e.parentNode
        //已经注入的标签无须再次处理
        if (imgUrl == null || targetContainer.getElementsByClassName("500px_dl").length > 0) {
            return;
        }
        div = document.createElement("DIV")
        var top = "0px"
        //注入下载按钮
        div.className = "500px_dl";
        div.setAttribute("style", "position:absolute;top:" + top + ";left:0px;width: 50px; height: 50px; z-index:1");
        div.innerHTML = '<img style="position:absolute;top:15px;bottom:0px;left:10px;right:0px;margin:auto;;width: 50px; height: 50px; object-fit: fill; pointer-events: none" src="data:image/png;base64,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" />';
        div.onclick = function (event) {
            if (!isClickCooling) {
                return
            }
            ADAPTATION_HOLDER.clickDownloadButtonEvent(window.location.host)
            isClickCooling = false
            setTimeout(function () {
                isClickCooling = true;
            }, 500);

            event.stopPropagation();
            var timestamp = Date.now()
            var type = "jpg"
            var data = {
                name: timestamp + ".jpg",
                sourceUrl: location.href,
                quality: "sd",
                thumbnailUrl: imgUrl,
                fromUrl: location.href,
                mediaUrlList: [imgUrl]
            }
            console.log("click data:: " + JSON.stringify(data))
            var resultData = {
                'status': 'time_line',
                'fromType': '500px',
                'showDialog': true,
                'parseType': type,
                'dataList': [data],
                'fromUrl': location.href,
                'dataSource': 'timeline===500px'
            }
            ADAPTATION_HOLDER.setParseBtnType("adapter", "websiteBtn", "detect_500px.js");
            receiveJsParseResult(JSON.stringify(resultData));
        }
        console.log("tagMedia: insertBefore")
        targetContainer.appendChild(div);
        targetContainer.setAttribute("tag", 1)
    }

    // 获取所有的img标签
    var findAllMedias = function (target) {
        // Elements__EditorsChoiceImg-y3wonq-14 Elements__Image-sc-1obh427-11
        // 首页图片
        var targets = target.getElementsByClassName("Elements__EditorsChoiceImg-y3wonq-14")
        var targetLength = targets.length;
        if (targetLength > 0) {
            for (var i = 0; i < targetLength; i++) {
                var imgE = targets[i]
                tagMedia(imgE, imgE.src)
            }
            return;
        } else {
            // 详情页 id: copyrightTooltipContainer || banner 第一个图片
            var detailContainer = target.getElementById("copyrightTooltipContainer")
            if (detailContainer != null) {
                var imgE = detailContainer.getElementsByTagName("img")[0]
                if (imgE != null) {
                    tagMedia(imgE, imgE.src)
                }
                return;
            }
        }
        // 列表页
        var lists = target.getElementsByClassName("Elements__Image-sc-1obh427-11")
        var listsLength = lists.length;
        if (listsLength > 0) {
            for (var i = 0; i < listsLength; i++) {
                var imgE = lists[i]
                tagMedia(imgE, imgE.src)
            }
        }
        // 详情页 banenr
        var bannerE = document.getElementById('banner');
        if (bannerE != null) {
            var style = bannerE.currentStyle || getComputedStyle(bannerE, 'style')
            var backgroundUrl = style.backgroundImage;
            var urlTagIndex = backgroundUrl.indexOf("url(")
            resultUrl = backgroundUrl.substring(urlTagIndex + 4, backgroundUrl.length - 1).replace(/"/g, "");
            if (resultUrl != null && resultUrl.length > 0) {
                tagMedia(bannerE, resultUrl)
            }
        }
    }

    var observerOptions = {
        childList: true, //story需要添加
        subtree: true,// 观察后代节点，默认为 false
    }

    var observer = new MutationObserver(function (mutations, observer) {
        mutations.forEach((mutation) => {
            switch (mutation.type) {
                case 'childList':
                    mutation.addedNodes.forEach((node) => {
                        if ((node.tagName == "DIV" || node.tagName == "SECTION") && node.className != "500px_dl") {
                            findAllMedias(document)
                        }
                    });
                    break;
            }
        });

    });
    console.log(" ======== 开始监听元素变化 ===== ")
    findAllMedias(document)
    observer.observe(document.body, observerOptions);
    console.log("完成加载 <<<<<<<<<<<<<<<<<<")
}());