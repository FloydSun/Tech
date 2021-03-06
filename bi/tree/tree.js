///<reference path="treeNode.ts"/>
var tree;
(function (tree_1) {
    var Tree = (function () {
        function Tree(id) {
            this.treeId = id;
            this.q().addClass("tree");
        }
        Tree.prototype.q = function (query) {
            if (query == undefined) {
                return $("#" + this.treeId);
            }
            else {
                return $("#" + this.treeId + " " + query);
            }
        };
        Tree.prototype.setOnClickListener = function (click) {
            this.onClick = click;
        };
        Tree.prototype.triggerClicked = function (treeNode) {
            if (treeNode.hasChildren()) {
                var ul = this.q("#" + treeNode.data.id + ">ul");
                if (treeNode.getData().extracted) {
                    treeNode.getData().extracted = false;
                    treeNode.data.height = ul.css("height");
                    ul.animate({
                        height: "0px"
                    }, 'fast');
                }
                else {
                    treeNode.getData().extracted = true;
                    ul.animate({
                        height: treeNode.getData().height
                    }, 'fast', function () {
                        ul.removeCss("height");
                    });
                }
            }
            this.q("div").removeClass("active");
            this.q("#" + treeNode.data.id + ">div").addClass("active");
            if (treeNode.data.click != undefined) {
                treeNode.data.click(treeNode);
            }
            if (undefined != this.onClick) {
                this.onClick(treeNode);
            }
        };
        Tree.prototype.render = function (tree) {
            var _this = this;
            this.q().empty();
            this.treeNodes = tree_1.TreeNode.valueOfAll(tree);
            this.q().append('<ul></ul>');
            for (var i = 0; i < this.treeNodes.length; ++i) {
                var curNode = this.treeNodes[i];
                this.q('>ul')
                    .append('<li id="' + curNode.getData().id + '"><div class="tree-item0">' +
                    curNode.getData().value + '</div></li>');
                this.q('>ul>li:last>div').click(curNode, function (event) {
                    _this.triggerClicked(event.data);
                });
                if (curNode.hasChildren()) {
                    this.q('>ul>li:last').append('<ul></ul>');
                    this.renderChildren(this.q('>ul>li:last>ul'), curNode, 1);
                    curNode.data.extracted = false;
                    curNode.data.height = this.q('>ul>li:last>ul').css("height");
                    this.q('>ul>li:last>ul').css("height", "0px");
                }
            }
        };
        Tree.prototype.renderChildren = function (ul, parent, depth) {
            var _this = this;
            var children = parent.subNodes;
            for (var i = 0; i < children.length; ++i) {
                var curNode = children[i];
                ul.append('<li id="' + curNode.getData().id + '"><div class="tree-item' + depth + '">' +
                    curNode.getData().value + '</div></li>');
                ul.children('li:last').children('div').click(curNode, function (event) {
                    _this.triggerClicked(event.data);
                });
                if (curNode.hasChildren()) {
                    ul.children('li:last').append('<ul></ul>');
                    this.renderChildren(ul.children('li:last').children('ul'), curNode, depth + 1);
                    curNode.data.extracted = false;
                    curNode.data.height = ul.children('li:last').children('ul').css("height");
                    ul.children('li:last').children('ul').css("height", "0px");
                }
            }
        };
        return Tree;
    })();
    tree_1.Tree = Tree;
})(tree || (tree = {}));
