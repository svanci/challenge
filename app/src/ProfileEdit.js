import React, { Component, useCallback } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label, Col, Row} from 'reactstrap';
import AppNavbar from './AppNavbar';
import {useDropzone} from 'react-dropzone'
import axios from 'axios'
import avatar from './static/default_avatar.png'

class GroupEdit extends Component {

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
            },
            isError: {
                firstName: '',
                lastName: '',
                occupation: '',
                linkedIn: '',
                description: ''
            }
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        const profile = await (await fetch(`/api/profiles/${localStorage.getItem("userId")}`)).json();
        this.setState({
            profile: profile
        });
    }

    handleChange(event) {
        event.preventDefault();
        const { name, value } = event.target;
        let profile = {...this.state.profile};
        profile[name] = value;
        this.setState({profile});

        this.validateField(name, value);

    }

    async handleSubmit(event) {
        event.preventDefault();
        if (formValid(this.state)) {
            const {profile} = this.state;

            const response = (await fetch(`/api/profiles/${localStorage.getItem("userId")}`, {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(profile)
            }));

            if(response.status === 200) {
                this.props.history.push('/')
            } else {
                // TODO manipulate the response from server and serve better information to user
                alert("There was a problem with validation! Check your fields");
            }
        }

    }



    validateField = (name, value) => {
        let isError = { ...this.state.isError };
        switch (name) {
            case "firstName":
                if(value.length < 1) {
                    isError.firstName = "First name is required";
                }
                else if(value.length > 16) {
                    isError.firstName = "First name should have max of 16 characters";
                }
                else {
                    isError.firstName = "";
                }
                break;
            case "lastName":
                if(value.length < 1) {
                    isError.lastName = "Last name is required";
                }
                else if(value.length > 16) {
                    isError.lastName = "Last name should have max of 16 characters";
                }
                else {
                    isError.lastName = "";
                }
                break;
            case "occupation":
                if(value.length > 30) {
                    isError.occupation = "Occupation field should contain max of 30 characters";
                }
                else {
                    isError.occupation = "";
                }
                break;
            case "linkedIn":
                if(value.length > 50) {
                    isError.linkedIn = "LinkedIn URL field should contain max of 50 characters";
                }
                else {
                    isError.linkedIn = "";
                }
                break;
            case "description":
                if(value.length > 255) {
                    isError.description = "Description should contain max of 255 characters";
                }
                else {
                    isError.description = "";
                }
                break;
            default:
                break;
        }

        this.setState({
            isError,
            [name]: value
        })
    };

    render() {
        const {profile} = this.state;
        const { isError } = this.state;

        return <div>
            <AppNavbar/>
            <Container>
                <Form onSubmit={this.handleSubmit}>
                    <Row xs="2">
                        <Col>
                            <h3>Personal Info</h3>
                            <FormGroup>
                                <Label for="id">ID</Label>
                                <Input type="text" name="id" id="id" value={profile.id || ''}
                                       onChange={this.handleChange} disabled  />
                            </FormGroup>
                            <FormGroup>
                                <Label for="firstName">First Name</Label>
                                <Input type="text" name="firstName" id="firstName" value={profile.firstName || ''}
                                       className={isError.firstName.length > 0 ? "is-invalid form-control" : "form-control"}
                                       onChange={this.handleChange} required/>
                                {isError.firstName.length > 0 && (
                                    <span className="invalid-feedback">{isError.firstName}</span>
                                )}
                            </FormGroup>
                            <FormGroup>
                                <Label for="lastName">Last Name</Label>
                                <Input type="text" name="lastName" id="lastName" value={profile.lastName || ''}
                                       className={isError.lastName.length > 0 ? "is-invalid form-control" : "form-control"}
                                       onChange={this.handleChange} required/>
                                {isError.lastName.length > 0 && (
                                    <span className="invalid-feedback">{isError.lastName}</span>
                                )}
                            </FormGroup>
                            <FormGroup>
                                <Label for="occupation">Occupation</Label>
                                <Input type="text" name="occupation" id="occupation" value={profile.occupation || ''}
                                       className={isError.occupation.length > 0 ? "is-invalid form-control" : "form-control"}
                                       onChange={this.handleChange}/>
                                {isError.occupation.length > 0 && (
                                    <span className="invalid-feedback">{isError.occupation}</span>
                                )}
                            </FormGroup>
                            <FormGroup>
                                <Label for="linkedIn">LinkedIn</Label>
                                <Input type="text" name="linkedIn" id="linkedIn" value={profile.linkedIn || ''}
                                       className={isError.linkedIn.length > 0 ? "is-invalid form-control" : "form-control"}
                                       onChange={this.handleChange}/>
                                {isError.linkedIn.length > 0 && (
                                    <span className="invalid-feedback">{isError.linkedIn}</span>
                                )}
                            </FormGroup>
                            <FormGroup>
                                <Label for="description">Description</Label>
                                <Input type="textarea" name="description" id="description" value={profile.description || ''}
                                       className={isError.description.length > 0 ? "is-invalid form-control" : "form-control"}
                                       onChange={this.handleChange}/>
                                {isError.description.length > 0 && (
                                    <span className="invalid-feedback">{isError.description}</span>
                                )}
                            </FormGroup>

                            <FormGroup>
                                <Button color="primary" type="submit">Save</Button>
                                <br/>
                                <Button color="secondary" tag={Link} to="/">Cancel</Button>
                            </FormGroup>
                        </Col>
                        <Col>
                            <h3>Profile picture</h3>
                            <img src={profile.profileImagePath === null ? avatar : `/api/profiles/${localStorage.getItem("userId")}/image`} alt="Profile" className="center"/>
                            <Dropzone />
                        </Col>
                    </Row>

                </Form>
            </Container>
        </div>
    }

}

// TODO could be a single class like AppNavbar.js
function Dropzone (){
    const onDrop = useCallback(acceptedFiles => {
        const file = acceptedFiles[0];
        const formData = new FormData();
        formData.append("file", file);

        // TODO send proper ID based on logged user
        const id = 1;
        axios.post(`/api/profiles/${id}/image`,
            formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data"
                }
            }
        ).then(() => {
            console.log("File uploaded successfully.");
            window.location.reload();
        }).catch( err => {
            // TODO handle error and let user know what happened
        })
    }, []);
    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop});

    return (
        <div {...getRootProps()}>
            <input {...getInputProps()} />
            {
                isDragActive ?
                    <p>Drop the file here ...</p> :
                    <p>Drag 'n' drop your profile picture here, or click to select file</p>
            }
        </div>
    )
}

const formValid = ({ isError, ...rest }) => {
    let valid = true;
    Object.values(isError).forEach(
        // if we have an error string set valid to false
        (val) => val.length > 0 && (valid = false)
    );
    return valid;
};

export default withRouter(GroupEdit);