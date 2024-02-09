import logging
import redis
from datetime import timedelta
from flask import Flask, request, jsonify
from forms import MessageForm
from settings import LOGGING_FORMAT, LOGGING_LEVEL, REDIS_HOST, REDIS_PORT, REDIS_DATABASE, REDIS_PASSWORD, API_TOKEN


logging.basicConfig(format=LOGGING_FORMAT, level=LOGGING_LEVEL)
logger = logging.getLogger(__name__)


app = Flask(__name__)
app.config['SECRET_KEY'] = 'W21HnIGT8AZTQLacDY89RFs0jhcA50qxssvTjLEpDwwqb4PnUM'
app.config['PERMANENT_SESSION_LIFETIME'] = timedelta(minutes=5)


#
# Views
#

@app.route('/', methods=('GET', 'POST'))
def index():
    if request.headers.get("Authorization") != "Bearer %s" % API_TOKEN:
        return jsonify({}), 401

    form = MessageForm(meta={'csrf': False})
    if request.method == "POST":
        if form.validate_on_submit():
            try:
                r = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DATABASE, password=REDIS_PASSWORD,
                                decode_responses=True)
                r.set(form.recipient.data, form.message.data)
                return jsonify({}), 200
            except Exception as e:
                logger.error(e)
                return jsonify({}), 500
        else:
            return jsonify(form.errors), 400
    else:
        recipient = request.args.get('recipient')
        if recipient:
            try:
                r = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DATABASE, password=REDIS_PASSWORD,
                                decode_responses=True)
                message = r.get(recipient)
                if message:
                    r.delete(recipient)
                    return jsonify({"message": message}), 200
                else:
                    return jsonify({}), 404
            except Exception as e:
                logger.error(e)
                return jsonify({}), 500
        else:
            return jsonify({"recipient": "This URL parameter is required"}), 400
