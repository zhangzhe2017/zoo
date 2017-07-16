'use strict';

import {doAction} from '../../../redux/actions/Action';
import FormService from '../../../services/FormService';
import Util from '../../../utils/Util';
import {FormList} from './FormList';

const {_} = window._external;

const ActionTypes = {

    formList: {

        getFormList(dispatch, getState) {
            const {pageType, currentPage, pageSize} = this || {};
            const params = {
                currentPage,
                pageSize
            };
            doAction(dispatch, ActionTypes.formList.changeState, {loading: true});
            FormService[pageType === 'myFormList' ? 'getMyFormList' : 'getAttendedActivityList']({
                ...(() => {
                    if (Util.isProxy()) {
                        if (pageType === 'myFormList') {
                            return {
                                url: `/form/getMyFormList${currentPage}.json`
                            };
                        } else {
                            return {
                                url: `/form/getAttendedActivityList${currentPage}.json`
                            };
                        }
                    } else {
                        return {};
                    }
                })(),
                data: params,
                success: (result = {}) => {
                    const {data = []} = result;
                    const {listData} = getState().formList;
                    const listDataLen = listData.length;
                    const resultListData = _.uniqBy(_.concat(listData, data), 'id');
                    const resultListDataLen = resultListData.length;
                    doAction(dispatch, ActionTypes.formList.changeState, {
                        finished: /*data.length < pageSize || */resultListDataLen === listDataLen,
                        listData: [...resultListData],
                        currentPage,
                        pageType
                    });
                    Util.later(() => {
                        const formList = FormList.instance;
                        formList && formList.renderList();
                    }, 1);
                },
                complete: () => {
                    doAction(dispatch, ActionTypes.formList.changeState, {loading: false});
                }
            });
        }

    }

};

export default ActionTypes;
