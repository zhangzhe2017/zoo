'use strict';

import CommonMixin from '../../../mixins/CommonMixin';

const {React, Component, PropTypes, connect, reactMixin, Result, Icon} = window._external;

@reactMixin.decorate(CommonMixin)
class Attention extends Component {

    render() {
        return (
            <div className="x-page">
                <Result
                    img={<Icon type="cross-circle-o" className="icon x-icon-error"/>}
                    title="请先关注【组局官】公众号"
                    message={
                        <div>
                            长按识别二维码<br/>
                            <img
                                className="x-marginTop-10 x-image"
                                src="http://zujuguan.oss-cn-shanghai.aliyuncs.com/gzh.jpg"
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
