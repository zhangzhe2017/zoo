'use strict';

const {_} = window._external;

export default {

    createNodesMap({nodesMap, data, parentId, keyFn, childrenKey}) {
        nodesMap = nodesMap || {};
        keyFn = keyFn || (node => node.id);
        childrenKey = childrenKey || 'children';
        data && _.forEach(data, node => {
            const key = keyFn(node);
            nodesMap[key] = node;
            node._parentId = parentId;
            if (node[childrenKey]) {
                this.createNodesMap({
                    nodesMap,
                    data: node[childrenKey],
                    parentId: key,
                    keyFn,
                    childrenKey
                });
            }
        });
        return nodesMap;
    },

    getParents({nodesMap, nodeId, keyFn}) {
        keyFn = keyFn || (node => node.id);
        let node = nodesMap[nodeId];
        if (!node) {
            return [];
        }
        if (nodeId == 'root') {
            return ['root'];
        }
        const pathArr = [];
        //pathArr.unshift(nodeId);
        let parentId = node._parentId;
        while (true) {
            if (parentId == 'root' || !parentId) {
                break;
            } else {
                node = nodesMap[parentId];
                pathArr.unshift(keyFn(node));
                parentId = node._parentId;
            }
        }
        return pathArr;
    }

};
