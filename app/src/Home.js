import React, { Component } from 'react';
import './Home.css';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import { Button, Container } from 'reactstrap';
import avatar from './static/default_avatar.png'

class Home extends Component {

    constructor(props) {
        super(props);
        this.state = {
            profile: {
                id:'',
                firstName: '',
                lastName: '',
                occupation: '',
                linkedIn: '',
                description: '',
                profileImagePath: ''

            }
        };
        // TODO: set user id based on logged user
        localStorage.setItem("userId", "1");
    }

    async componentDidMount() {
        const profile = await (await fetch(`/api/profiles/${localStorage.getItem("userId")}`)).json();
        localStorage.setItem("profile", profile);
        this.setState({profile: profile});
    }

    render() {
        const {profile} = this.state;
        const fullName = profile.firstName + ' ' + profile.lastName;
        let linkedIn = profile.linkedIn;

        if(!linkedIn.startsWith("http")) {
            linkedIn = "https://" + linkedIn;
        }
        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="card">
                        <img src={profile.profileImagePath === null ? avatar : `/api/profiles/${localStorage.getItem("userId")}/image`} alt="Profile" className="center"/>
                            <h1>{fullName}</h1>
                            <p className="title">{profile.occupation}</p>
                        <a target="_blank" rel="noopener noreferrer" href={linkedIn} className="fa fa-linkedin"> </a>
                            <p>{profile.description}</p>
                    </div>
                    <Button className="update-button" color="link" ><Link to="/profileEdit">Edit profile</Link></Button>
                </Container>
            </div>
        );
    }
}

export default Home;