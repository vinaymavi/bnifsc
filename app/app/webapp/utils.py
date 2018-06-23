from django.template import Context, Template
import logging


def tempate_to_string(template_str, context):
    template = Template(template_str)
    context = Context(context)
    template_output = template.render(context)
    logging.info(template_output)
    return template_output


def process_seo_headings(heading, context):
    heading['h1_template'] = tempate_to_string(heading['h1_template'], context)
    heading['h2_template'] = tempate_to_string(heading['h2_template'], context)
    return heading


def process_seo_data(data, context):
    process_seo_headings(data['heading_template'], context)
    return data


def process_seo_components(components, context):
    pass


def process_header_meta(header, context):
    pass