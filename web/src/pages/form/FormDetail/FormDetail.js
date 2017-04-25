'use strict';

import style from './FormDetail.scss';
import {doAction} from '../../../redux/actions/Action';
import ActionTypes from '../../../redux/actions/ActionTypes';
import CommonMixin from '../../../mixins/CommonMixin';
import ListDetailItem from '../../../components/ListDetailItem/ListDetailItem';

const {React, Component, PropTypes, connect, reactMixin, List, _, Button} = window._external;

@reactMixin.decorate(CommonMixin)
class FormDetail extends Component {

    static defaultState = {
        loading: false,
        type: '',
        title: '',
        fields: [],
        fieldValues: {},
        registered: false,
        creatorNickName: '',
        creatorWxid: '',
        attenderList: []
    };

    bindFns = ['handleRegisterBtnClick'];

    init() {
        const {dispatch, location} = this.props;
        const {query} = location;
        doAction(dispatch, ActionTypes.formDetail.getForm, {
            id: query.formId
        });
    }

    reset() {
        const {dispatch} = this.props;
        doAction(dispatch, ActionTypes.formDetail.replaceState, FormDetail.defaultState);
    }

    handleRegisterBtnClick() {
        const {dispatch, registered} = this.props;
        doAction(dispatch, ActionTypes.formDetail.register, {
            register: !registered,
            formDetail: this
        });
    }

    handleImageClick(url, urls) {
        wx.previewImage({
            current: url,
            urls
        });
    }

    render() {
        const {
            type, title, fields, fieldValues, registered, creatorNickName, creatorWxid, attenderList, wxid
        } = this.props;
        let realTitle = null;
        if (type === 'activity') {
            realTitle = fieldValues.title;
        } else {
            realTitle = title;
        }
        const items = [];
        _.forEach(fields, field => {
            const {name, label, type} = field;
            if (name === 'title') {
                return;
            }
            let content = null;
            if (type === 'image') {
                content = [];
                const urls = fieldValues[name];
                _.forEach(urls, (url, index) => {
                    content.push(
                        <div key={index} style={{marginTop: '20px'}}>
                            <img
                                style={{width: '100%', height: 'auto'}}
                                src={url}
                                onClick={this.handleImageClick.bind(this, url, urls)}
                            />
                        </div>
                    );
                });
            } else {
                content = fieldValues[name];
            }
            items.push(<ListDetailItem key={name} type={type} label={label} content={content}/>);
        });
        return (
            <div className="x-page">
                {
                    realTitle ?
                        <h2 className={style.title}>{realTitle}</h2> : ''
                }
                {
                    items.length ?
                        <List className="x-detail-list">
                            <ListDetailItem label="发起人" content={creatorNickName}/>
                            {items}
                            <ListDetailItem
                                label={`已报名`}
                                content={`(${attenderList.length}人) ${attenderList.join(', ')}`}
                            />
                        </List> : ''
                }
                {
                    type === 'activity' ?
                        <Button
                            type={registered ? 'default' : 'primary'}
                            style={{marginTop: '0.25rem'}}
                            onClick={this.handleRegisterBtnClick}
                            disabled={wxid === creatorWxid}
                        >
                            {registered ? '取消报名' : '我要报名'}
                        </Button> : ''
                }
            </div>
        );
    }

}

FormDetail.propTypes = {
    location: PropTypes.object.isRequired
};

export {FormDetail};

export default connect(state => {
    const {appData, formDetail} = state;
    const {wxid} = appData;
    return {
        ...formDetail,
        wxid
    };
})(FormDetail);
