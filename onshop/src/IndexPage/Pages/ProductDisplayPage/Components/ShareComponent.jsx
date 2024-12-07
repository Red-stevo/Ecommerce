import React, {useEffect, useState} from 'react';
import {
    EmailIcon,
    EmailShareButton,
    FacebookIcon,
    FacebookShareButton, TelegramIcon,
    TelegramShareButton,
    TwitterIcon,
    TwitterShareButton,
    WhatsappIcon,
    WhatsappShareButton
} from "react-share";
import {CopyToClipboard} from "react-copy-to-clipboard/src";
import Modal from "react-bootstrap/Modal";


const ShareComponent = (props) => {
const [copy, setCopy] = useState(false);
const productUrl = window.location.href;

    useEffect(() => {

        const  resentCopied = () => {
            setTimeout(() => {
                setCopy(() => false)
            }, 5000)
        }


        if (copy) resentCopied();

    }, [copy]);

const handleCopy = () => {
  setCopy((prevState) => !prevState);
};

return (
    <Modal{...props} size="md" aria-labelledby="contained-modal-title-vcenter" centered >
        <Modal.Header closeButton>
            <Modal.Title id="contained-modal-title-vcenter">
                Share Product
            </Modal.Title>
        </Modal.Header>
        <Modal.Body className={"share-product-modal"} >
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

            <TelegramShareButton url={productUrl}>
                <TelegramIcon size={30} round />
            </TelegramShareButton>


            <CopyToClipboard text={productUrl}>
                <button className={`${!copy ? " copy-button " : " copied-button "}`}
                        onClick={() => setCopy(prevState => !prevState)}>
                    {copy ? "Copied!": "Copy Link"}
                </button>
            </CopyToClipboard>

        </Modal.Body>
    </Modal>
    );
};

export default ShareComponent;




