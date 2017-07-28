/**
 * Copyright 2014 - 2017 Cognizant Technology Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
jQuery.fn.centerHorizontally = function () {
    this.css("position", "absolute");
    this.css("top", Math.max(0, $(window).scrollTop() + 80) + "px");
    console.log($(this).outerWidth());
    this.css("left", Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) +
            $(window).scrollLeft()) + "px");
    return this;
}


function loadImage(imagePath, callback) {
    var img = new Image();
    img.onload = function () {
        callback(this, this.width, this.height);
    };
    img.onerror = function () {
        console.log("Cannot load image", "Path: " + imagePath);
    };
    img.src = imagePath;
}


function setStatusLink(stat, actualImagePath, expectedImagePath, mapImagePath, objects, screenshot)
{
    if ((actualImagePath && actualImagePath !== "") || (screenshot && screenshot !== "")) {
	var link=screenshot?screenshot.indexOf(".html")!==-1 ? ("' href='." + screenshot) : "":"";
        return "<a class='exe table " + stat.toUpperCase() +
                "' data-actual-image='" + actualImagePath
                + "' data-expected-image='" + expectedImagePath
                + "' data-map-image='" + mapImagePath
                + "' data-screenshot-image='." + screenshot
				+ link
                + "' data-objects-area='" + objects
                + "' style='cursor:pointer;'>" + stat + "</a>";
    }
    return stat;
}

function setStatus() {
    var $this = $(this);
    var actualImagePath = $this.attr("data-actual-image");
    var expectedImagePath = $this.attr("data-expected-image");
    var mapImagePath = $this.attr("data-map-image");
    var screenshot = $this.attr("data-screenshot-image");
    var objects = $this.attr("data-objects-area");
    if (actualImagePath && actualImagePath !== "undefined") {
        showImageComparison(actualImagePath, expectedImagePath, mapImagePath);
    }
    else if (screenshot && screenshot !== "undefined") {
        showScreenShot(objects, screenshot);
    }
}


function showImageComparison(actualImagePath, expectedImagePath, mapImagePath) {
    loadImage(actualImagePath, function (actualImage, actualImageWidth, actualImageHeight) {
        loadImage(expectedImagePath, function (expectedImage, expectedImageWidth, expectedImageHeight) {
            loadImage(mapImagePath, function (mapImage, mapImageWidth, mapImageHeight) {
                showPopup(setImages({
                    actual: actualImagePath,
                    expected: expectedImagePath,
                    map: mapImagePath
                }));
            });
        });
    });
    return false;
}

function showScreenShot(objects, screenshot) {
    showScreenshotWithObjects(screenshot, this.width, this.height, objects);
}

function onImageComparisonClick() {
    var $this = $(this);
    var actualImagePath = $this.attr("data-actual-image");
    var expectedImagePath = $this.attr("data-expected-image");
    var mapImagePath = $this.attr("data-map-image");

    loadImage(actualImagePath, function (actualImage, actualImageWidth, actualImageHeight) {
        loadImage(expectedImagePath, function (expectedImage, expectedImageWidth, expectedImageHeight) {
            loadImage(mapImagePath, function (mapImage, mapImageWidth, mapImageHeight) {
                showPopup(setImages({
                    actual: actualImagePath,
                    expected: expectedImagePath,
                    map: mapImagePath
                }));
            });
        });
    });
    return false;
}

function showScreenshotWithObjects(screenshotPath, width, height, objects) {
    loadImage(screenshotPath, function (actualImage, actualImageWidth, actualImageHeight) {
        $('#screenShotImage').attr("src", screenshotPath);
        showPopup(setScreenshotImages({
            screenshot: screenshotPath,
            objects: objects
        }));

    });
}

function setScreenshotImages(data) {
    var screenshotElement = $("div.screenshot-canvas");
    if (data.objects && data.objects !== "undefined")
        appendObjects(screenshotElement, JSON.parse(data.objects));
    screenshotElement.show();
    return screenshotElement;
}

function setImages(data) {
    $('#actual').attr("src", data.actual);
    $('#expected').attr("src", data.expected);
    $('#comparison').attr("src", data.map);
    $("div.image-comparison").show();
    return $("div.image-comparison");
}

function showShadow() {
    $("#screen-shadow").fadeIn();
}
function hideShadow() {
    $("#screen-shadow").fadeOut();
    removeAllChild($('div.canvas-rect'));
}
function showPopup(html) {
    showShadow();
    $("#popup .popup-content").html(html);
    $("#popup").centerHorizontally().fadeIn('fast');
}

function hidePopup() {
    $("div.image-comparison").hide();
    $("div.screenshot-canvas").hide();
    hideShadow();
    $("#popup").fadeOut();
}

function onPopupCloseClick() {
    hideShadow();
    $(this).closest(".popup").fadeOut();
    return false;
}
var colors = [
    "#B55CFF", "#FF5C98", "#5C9AFF", "#5CE9FF", "#5CFFA3", "#98FF5C", "#FFE95C", "#FFA05C"
];
function appendObjects(screenshotElement, objects)
{
    objects.forEach(function (object, index) {
        screenshotElement.append(getObjectCanvas(object.name, JSON.parse(object.area), colors[index % colors.length]));
    });
}

function getObjectCanvas(name, area, color)
{
    return  "<div class='canvas-rect' style='left: " + area[0] + "px; " +
            "top:" + area[1] + "px;" +
            "width: " + area[2] + "px;" +
            "height:" + area[3] + "px; border-color:" + color + ";'>"
            + "<div class='canvas-rect-wrapper'>"
            + "<div class='canvas-rect-hint' style='background: " + color + ";'>" + name + "</div>"
            + "</div>"
            + "</div>"
}

function removeAllChild(el) {
    try {
        for (var i = 0; i < el.length; i++)
        {
            el[i].remove();
        }
    }
    catch (e) {
    }
}