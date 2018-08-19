window.toStr = (m) => {
    let escaped = {};
    for(let k in m) {
        let v = m[k];
        if(typeof v === 'string'){
            escaped[k] = v.replace(';', ' ');
        }
        else {
            escaped[k] = v;
        }
    }
    return AFRAME.utils.styleParser.stringify(escaped);
}

class VRPedia extends React.Component {

    static instance;

    constructor(props) {
        super(props);
        this.state = {
            loading:true,
            subject:null
        };
        VRPedia.instance = this;
    }

    loadSubject(subjectUri) {
        this.setState({loading:true});
        $.get('/api/entity?uri='+encodeURI(subjectUri)).done(this.setSubject.bind(this));
    }

    setSubject(subject) {
        this.setState({loading:false, subject: subject});
    }

    render() {
        if(this.state.loading) {
            return <a-text value="Loading" position="0 0 -5"/>
        }

        return (
            <a-entity>
                <a-entity position="-3.5 4 -8">
                <a-entity id="subjectTitle"
                            position="0.1 0.5 0.01"
                            text={`anchor:left; width:10; value:${this.state.subject.label}`}
                            text-background="paddingX:0.1"
                            geometry="primitive:plane;">
                </a-entity>
                <a-entity id="subjectAbstract"
                            class="collidable"
                            grab-scroll=""
                            text-texture={`font: roboto; width: 12; fontSize: 64; text:${this.state.subject.abstract.replace(/[:;]/g, ' ')}`}
                            >
                </a-entity>
                <a-plane id="subjectImage"
                        fit-texture="use-factor:true;pixels-per-unit:200; max-width:2.0;"
                        material={`src:url(${this.state.subject.image})`}
                        super-anchor="-0.5 -0.5 0"
                        position="0 0 0">
                </a-plane>
                </a-entity>
                <a-entity position="0 0.5 0" rotation="0 0 0" scale="0.5 0.5 0.5">
                <RelationshipsList references={this.state.subject.references}/>
                </a-entity>
            </a-entity>
        );
    }
}
