import React, {useState} from 'react';
import {
    EmailIcon,
    EmailShareButton,
    FacebookIcon,
    FacebookShareButton,
    TwitterIcon,
    TwitterShareButton,
    WhatsappIcon,
    WhatsappShareButton
} from "react-share";
import {CopyToClipboard} from "react-copy-to-clipboard/src";
import Modal from "react-bootstrap/Modal";
import {Button} from "react-bootstrap";

const ShareComponent = (props) => {
const [copy, setCopy] = useState(false);
const productUrl = window.location.href

const handleCopy = (props) => {
  setCopy((prevState) => !prevState);
};


    return (
        <Modal{...props} size="md" aria-labelledby="contained-modal-title-vcenter" centered >
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                    Share Product
                </Modal.Title>
            </Modal.Header>
            <Modal.Body className={"user-details-modal"} >
                <WhatsappShareButton url={productUrl} >
                    <WhatsappIcon size={30} round />
                </WhatsappShareButton>

                <FacebookShareButton url={productUrl} >
                    <FacebookIcon size={30} round />
                </FacebookShareButton>

                <TwitterShareButton url={productUrl} >
                    <TwitterIcon size={30} round />
                </TwitterShareButton>

                <EmailShareButton url={productUrl}>
                    <EmailIcon size={30} round />
                </EmailShareButton>

                <CopyToClipboard text={productUrl}>
                    <button onClick={() => setCopy(prevState => !prevState)}>
                        {copy ? "Copied!": "Copy Link"}
                    </button>
                </CopyToClipboard>
            </Modal.Body>
        </Modal>
    );
};

export default ShareComponent;




