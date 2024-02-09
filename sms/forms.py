import logging
from flask_wtf import FlaskForm
from settings import LOGGING_FORMAT, LOGGING_LEVEL
from wtforms import StringField
from wtforms.validators import DataRequired


from flask_wtf.form import _is_submitted, _Auto
from flask import request
from werkzeug.datastructures import CombinedMultiDict
from werkzeug.datastructures import ImmutableMultiDict



logging.basicConfig(format=LOGGING_FORMAT, level=LOGGING_LEVEL)
logger = logging.getLogger(__name__)


class MessageForm(FlaskForm):
    recipient = StringField(label="recipient", default=None, validators=[DataRequired()])
    message = StringField(label="message", default=None, validators=[DataRequired()])

    class Meta(FlaskForm.Meta):
        def wrap_formdata(self, form, formdata):
            if formdata is _Auto:
                if _is_submitted():
                    if request.files:
                        return CombinedMultiDict((request.files, request.form))
                    elif request.form:
                        return request.form
                    elif request.is_json:
                        return ImmutableMultiDict(request.get_json())
                    elif not request.mimetype:
                        return ImmutableMultiDict(request.get_json(force=True, silent=True))
                return None
            return formdata
