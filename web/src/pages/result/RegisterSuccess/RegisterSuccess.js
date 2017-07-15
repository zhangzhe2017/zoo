'use strict';

import CommonMixin from '../../../mixins/CommonMixin';

const {React, Component, PropTypes, connect, reactMixin, Result, Icon, _} = window._external;

@reactMixin.decorate(CommonMixin)
class RegisterSuccess extends Component {

    static defaultState = {
        qrCodeUrls: []
    };

    /*reset() {
        const {dispatch} = this.props;
        doAction(dispatch, ActionTypes.registerSuccess.replaceState, RegisterSuccess.defaultState);
    }*/

    handleImageClick(url, urls) {
        wx.previewImage({
            current: url,
            urls
        });
    }

    render() {
        const {qrCodeUrls} = this.props;
        const items = [];
        _.forEach(qrCodeUrls, qrCodeUrl => {
            items.push(
                <img
                    className="x-marginTop-10 x-image"
                    src={qrCodeUrl}
                    onClick={this.handleImageClick.bind(this, qrCodeUrl, qrCodeUrls)}
                />
            );
        });
        const message = (
            <div>
                长按识别二维码<br/>
                {items}
            </div>
        );
        return (
            <div className="x-page">
                <Result
                    img={<Icon type="check-circle" className="icon x-icon-success"/>}
                    title="报名成功"
                    message={message}
                />
            </div>
        );
    }

}

RegisterSuccess.propTypes = {
    location: PropTypes.object.isRequired
};

export {RegisterSuccess};

export default connect(state => state.registerSuccess)(RegisterSuccess);
