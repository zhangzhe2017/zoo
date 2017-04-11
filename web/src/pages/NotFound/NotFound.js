'use strict';

import style from './NotFound.scss';
import CommonMixin from '../../mixins/CommonMixin';

const {React, Component, PropTypes, connect, reactMixin} = window._external;

@reactMixin.decorate(CommonMixin)
class NotFound extends Component {

    render() {
        return (
            <div>
                <div className={style.normal}>
                    <div className={style.container}>
                        <h1 className={style.title}>404</h1>
                        <p className={style.desc}>未找到该页面</p>
                    </div>
                </div>
            </div>
        );
    }

}

NotFound.propTypes = {
    location: PropTypes.object.isRequired
};

export {NotFound};

export default connect(state => ({}))(NotFound);
