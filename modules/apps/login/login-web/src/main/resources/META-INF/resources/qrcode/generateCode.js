import QRCode from 'qrcode';

export default (containerId, message) => {
	QRCode.toDataURL(message)
		.then((url) => {
			const image = document.createElement('img');
			image.setAttribute('src', url);

			const container = document.getElementById(containerId);
			container.appendChild(image);
		})
		.catch((err) => {
			console.error(err);
		});
};
