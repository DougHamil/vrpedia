class ReactButton extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            text: 'Test',
            count:0
        }
    }

    render(){
        return <a-entity class="collidable"
        text={'value:'+this.state.count} geometry="primitive:plane;width:4"
        onClick={()=> this.setState({count: this.state.count+1})}>
            </a-entity>;
    }
}
