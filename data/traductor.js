#!/usr/bin/env node

const fs = require("fs");
const strData = fs.readFileSync(0, "utf-8");

const data = JSON.parse(strData);

let id = 0;
function getId() {
    id++;
    return id;
}

/**
 * @param {Object} obj : the object to print
 * @param {String} currentId : the id of the current obj to print 
 */
function printAttr(obj, currentId, preKey) {
    preKey = preKey || "";
    Object.keys(obj).forEach(key => {
        if(Array.isArray(obj[key])) {
            //key is array -> add an list edge
            const newId = getId();
            console.log(escBTag`ast_edge ${currentId} ${newId} ${preKey + key}`);
            console.log(escBTag`type ${newId} list`);
            printAttr(obj[key], newId);
        } else if(key === "type") {
            console.log(escBTag`${key} ${currentId} ${obj[key]}`);
        } else if(key === "start" || key === "end") {
            const name = key == "start" ? "begin" : "end";
            console.log(escBTag`char_${name} ${currentId} ${obj[key]}`);
        } else if(key === "loc") {
            console.log(escBTag`line_begin ${currentId} ${obj.loc.start.line}`);
            console.log(escBTag`column_begin ${currentId} ${obj.loc.start.column}`);
            console.log(escBTag`line_end ${currentId} ${obj.loc.end.line}`);
            console.log(escBTag`column_end ${currentId} ${obj.loc.end.column}`);
        } else if(typeof obj[key] === 'object' && obj[key] !== null) {
            if(obj[key].type) {
                const newId = getId()
                console.log(escBTag`ast_edge ${currentId} ${newId} ${preKey + key}`);
                printAttr(obj[key], newId);
            } else {
                printAttr(obj[key], currentId, preKey + key + ".");
            }
        } else {
            console.log(escBTag`node_attr ${currentId} ${preKey + key} ${obj[key]}`) //besoin d'escape " " "\n" "\"...
        }
    });
}

const rootId = getId();
console.log(`attr ast_root ` + rootId);
printAttr(data, rootId);

function escBTag(strings, ...expr) {
    return expr.reduce((acc, expr, i) => acc + strings[i] + escB(expr), "") + strings[strings.length - 1];
}

function escB(val) {
    return ("" + val)
        .replace(/\\/g, "\\\\")
        .replace(/ /g, "\\ ")
        .replace(/\n/g, "\\\n")
}