'use strict';

import {doAction} from '../../../redux/actions/Action';
import ActionTypes from '../../../redux/actions/ActionTypes';
import CommonMixin from '../../../mixins/CommonMixin';
import Util from '../../../utils/Util';

const {
    React, Component, PropTypes, connect, reactMixin, ActivityIndicator, Card, _, $, Link
} = window._external;

@reactMixin.decorate(CommonMixin)
class FormList extends Component {

    static pageSize = 10;
    static defaultState = {
        loading: false,
        finished: false,
        listData: [],
        currentPage: 0
    };

    pageTitle = '表单列表';

    getViewportHeight() {
        var prop = 'clientHeight',
            win = window,
            doc = win.document,
            body = doc.body,
            documentElement = doc.documentElement,
            documentElementProp = documentElement[prop];
        // 标准模式取 documentElement
        // backcompat 取 body
        return doc.compatMode === 'CSS1Compat'
            && documentElementProp ||
            body && body[prop] || documentElementProp;
    }

    renderList() {
        const {dispatch, location, loading, finished, currentPage} = this.props;
        if (loading || finished) {
            return;
        }
        const top = $('#loadingDiv').offset().top;
        const scrollTop = $(document).scrollTop();
        if (top - scrollTop < this.getViewportHeight()) {
            const {query} = location;
            const {pageType} = query;
            doAction(dispatch, ActionTypes.formList.getFormList, {
                pageType,
                currentPage: currentPage + 1,
                pageSize: FormList.pageSize
            });
        }
    }

    init() {
        const {location} = this.props;
        const {query} = location;
        const {pageType} = query;
        Util.later(() => {
            this.renderList();
        }, 1);
        $(window).on('scroll.FormList', Util.buffer(this.renderList).bind(this));
        this.pageTitle = (pageType === 'myFormList' ? '我发起的活动' : '我参加的活动');
    }

    reset() {
        const {dispatch} = this.props;
        doAction(dispatch, ActionTypes.formList.replaceState, FormList.defaultState);
        $(window).off('scroll.FormList');
    }

    render() {
        const {location, finished, listData} = this.props;
        const {query} = location;
        const {pageType} = query;
        const items = [];
        _.forEach(listData, row => {
            const {id, title, createTime} = row;
            items.push(
                <Link
                    key={id}
                    to={{
                        pathname: '/form/view',
                        query: {formId: id}
                    }}
                >
                    <Card className="x-card">
                        <Card.Body>
                            <div>标题：{title}</div>
                        </Card.Body>
                    </Card>
                </Link>
            );
        });
        return (
            <div className="x-page">
                <h2 className="x-title">{pageType === 'myFormList' ? '我发起的活动' : '我参加的活动'}</h2>
                {items}
                <div id="loadingDiv" className="x-margin-20-0">
                    {
                        finished ? <div className="x-noMore">没有更多了</div> :
                            <ActivityIndicator size="large" className="x-activity-indicator"/>
                    }
                </div>
            </div>
        );
    }

}

FormList.propTypes = {
    location: PropTypes.object.isRequired
};

export {FormList};

export default connect(state => state.formList)(FormList);
