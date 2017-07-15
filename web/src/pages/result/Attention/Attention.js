'use strict';

import CommonMixin from '../../../mixins/CommonMixin';
import Util from '../../../utils/Util';
import infoIcon from './info.svg';

const {React, Component, PropTypes, connect, reactMixin, Result, Icon} = window._external;

@reactMixin.decorate(CommonMixin)
class Attention extends Component {

    handleImageClick(url, urls) {
        wx.previewImage({
            current: url,
            urls
        });
    }

    render() {
        const qrCodeUrl = Util.qrCodeUrl;
        return (
            <div className="x-page">
                <Result
                    img={<Icon type={infoIcon} className="icon x-icon-info"/>}
                    title="请先关注【组局官】公众号"
                    message={
                        <div>
                            长按识别二维码<br/>
                            <img
                                className="x-marginTop-10 x-image"
                                src={qrCodeUrl}
                                onClick={this.handleImageClick.bind(this, qrCodeUrl, [qrCodeUrl])}
                            />
                        </div>
                    }
                />
            </div>
        );
    }

}

Attention.propTypes = {
    location: PropTypes.object.isRequired
};

export {Attention};

export default connect(state => ({}))(Attention);
